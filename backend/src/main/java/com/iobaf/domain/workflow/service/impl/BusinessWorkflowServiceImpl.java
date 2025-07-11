package com.iobaf.domain.workflow.service.impl;

import com.iobaf.domain.workflow.entity.*;
import com.iobaf.domain.workflow.service.BusinessWorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 业务流程服务实现类
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Slf4j
@Service
public class BusinessWorkflowServiceImpl implements BusinessWorkflowService {

    // 模拟数据存储，实际项目中应该使用数据库
    private final Map<Long, SalesOpportunity> salesOpportunities = new HashMap<>();
    private final Map<Long, Contract> contracts = new HashMap<>();
    private final Map<Long, Project> projects = new HashMap<>();
    private final Map<Long, Invoice> invoices = new HashMap<>();
    private final Map<Long, PurchaseRequest> purchaseRequests = new HashMap<>();
    private final Map<Long, ExpenseRequest> expenseRequests = new HashMap<>();
    private final Map<Long, Approval> approvals = new HashMap<>();

    private long salesOpportunityIdCounter = 1;
    private long contractIdCounter = 1;
    private long projectIdCounter = 1;
    private long invoiceIdCounter = 1;
    private long purchaseRequestIdCounter = 1;
    private long expenseRequestIdCounter = 1;
    private long approvalIdCounter = 1;

    // ==================== 销售到收款流程 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createSalesOpportunity(SalesOpportunity opportunity) {
        log.info("创建销售机会，机会信息：{}", opportunity);
        
        try {
            opportunity.setId(salesOpportunityIdCounter++);
            opportunity.setOpportunityNo("OPP" + String.format("%06d", opportunity.getId()));
            opportunity.setCreatedAt(LocalDateTime.now());
            opportunity.setUpdatedAt(LocalDateTime.now());
            
            if (opportunity.getStatus() == null) {
                opportunity.setStatus("LEAD");
            }
            
            salesOpportunities.put(opportunity.getId(), opportunity);
            
            log.info("销售机会创建成功，ID={}", opportunity.getId());
            return true;
        } catch (Exception e) {
            log.error("创建销售机会失败", e);
            throw new RuntimeException("创建销售机会失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSalesOpportunityStatus(Long opportunityId, String status) {
        log.info("更新销售机会状态，opportunityId={}, status={}", opportunityId, status);
        
        try {
            SalesOpportunity opportunity = salesOpportunities.get(opportunityId);
            if (opportunity == null) {
                throw new RuntimeException("销售机会不存在");
            }
            
            opportunity.setStatus(status);
            opportunity.setUpdatedAt(LocalDateTime.now());
            
            log.info("销售机会状态更新成功");
            return true;
        } catch (Exception e) {
            log.error("更新销售机会状态失败", e);
            throw new RuntimeException("更新销售机会状态失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createContract(Contract contract) {
        log.info("创建合同，合同信息：{}", contract);
        
        try {
            contract.setId(contractIdCounter++);
            contract.setContractNo("CON" + String.format("%06d", contract.getId()));
            contract.setCreatedAt(LocalDateTime.now());
            contract.setUpdatedAt(LocalDateTime.now());
            
            if (contract.getStatus() == null) {
                contract.setStatus("DRAFT");
            }
            if (contract.getApprovalStatus() == null) {
                contract.setApprovalStatus("PENDING");
            }
            
            contracts.put(contract.getId(), contract);
            
            log.info("合同创建成功，ID={}", contract.getId());
            return true;
        } catch (Exception e) {
            log.error("创建合同失败", e);
            throw new RuntimeException("创建合同失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveContract(Long contractId, Long approverId, boolean approved, String comments) {
        log.info("合同审批，contractId={}, approverId={}, approved={}", contractId, approverId, approved);
        
        try {
            Contract contract = contracts.get(contractId);
            if (contract == null) {
                throw new RuntimeException("合同不存在");
            }
            
            // 创建审批记录
            Approval approval = new Approval();
            approval.setId(approvalIdCounter++);
            approval.setBusinessType("CONTRACT");
            approval.setBusinessId(contractId);
            approval.setApproverId(approverId);
            approval.setApprovalLevel(1);
            approval.setResult(approved ? "APPROVED" : "REJECTED");
            approval.setComments(comments);
            approval.setApprovedAt(LocalDateTime.now());
            approval.setCreatedAt(LocalDateTime.now());
            approval.setUpdatedAt(LocalDateTime.now());
            
            approvals.put(approval.getId(), approval);
            
            // 更新合同状态
            if (approved) {
                contract.setApprovalStatus("APPROVED");
                contract.setStatus("APPROVED");
            } else {
                contract.setApprovalStatus("REJECTED");
                contract.setStatus("DRAFT");
            }
            contract.setUpdatedAt(LocalDateTime.now());
            
            log.info("合同审批完成");
            return true;
        } catch (Exception e) {
            log.error("合同审批失败", e);
            throw new RuntimeException("合同审批失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createProject(Project project) {
        log.info("创建项目，项目信息：{}", project);
        
        try {
            project.setId(projectIdCounter++);
            project.setProjectNo("PRJ" + String.format("%06d", project.getId()));
            project.setCreatedAt(LocalDateTime.now());
            project.setUpdatedAt(LocalDateTime.now());
            
            if (project.getStatus() == null) {
                project.setStatus("PLANNING");
            }
            if (project.getProgress() == null) {
                project.setProgress(BigDecimal.ZERO);
            }
            
            projects.put(project.getId(), project);
            
            log.info("项目创建成功，ID={}", project.getId());
            return true;
        } catch (Exception e) {
            log.error("创建项目失败", e);
            throw new RuntimeException("创建项目失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProjectProgress(Long projectId, BigDecimal progress) {
        log.info("更新项目进度，projectId={}, progress={}", projectId, progress);
        
        try {
            Project project = projects.get(projectId);
            if (project == null) {
                throw new RuntimeException("项目不存在");
            }
            
            project.setProgress(progress);
            project.setUpdatedAt(LocalDateTime.now());
            
            // 根据进度更新项目状态
            if (progress.compareTo(BigDecimal.valueOf(100)) >= 0) {
                project.setStatus("COMPLETED");
            } else if (progress.compareTo(BigDecimal.valueOf(50)) >= 0) {
                project.setStatus("EXECUTING");
            }
            
            log.info("项目进度更新成功");
            return true;
        } catch (Exception e) {
            log.error("更新项目进度失败", e);
            throw new RuntimeException("更新项目进度失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Invoice generateInvoice(Long projectId, BigDecimal amount) {
        log.info("生成发票，projectId={}, amount={}", projectId, amount);
        
        try {
            Project project = projects.get(projectId);
            if (project == null) {
                throw new RuntimeException("项目不存在");
            }
            
            Contract contract = contracts.get(project.getContractId());
            if (contract == null) {
                throw new RuntimeException("合同不存在");
            }
            
            Invoice invoice = new Invoice();
            invoice.setId(invoiceIdCounter++);
            invoice.setInvoiceNo("INV" + String.format("%06d", invoice.getId()));
            invoice.setProjectId(projectId);
            invoice.setCustomerId(contract.getCustomerId());
            invoice.setInvoiceType("SALES");
            invoice.setAmount(amount);
            invoice.setReceivedAmount(BigDecimal.ZERO);
            invoice.setInvoiceDate(java.time.LocalDate.now());
            invoice.setDueDate(java.time.LocalDate.now().plusDays(30));
            invoice.setStatus("ISSUED");
            invoice.setPaymentStatus("UNPAID");
            invoice.setCreatedBy(project.getManagerId());
            invoice.setCreatedAt(LocalDateTime.now());
            invoice.setUpdatedAt(LocalDateTime.now());
            
            invoices.put(invoice.getId(), invoice);
            
            log.info("发票生成成功，ID={}", invoice.getId());
            return invoice;
        } catch (Exception e) {
            log.error("生成发票失败", e);
            throw new RuntimeException("生成发票失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmPayment(Long invoiceId, BigDecimal paymentAmount, String paymentMethod) {
        log.info("确认收款，invoiceId={}, paymentAmount={}, paymentMethod={}", invoiceId, paymentAmount, paymentMethod);
        
        try {
            Invoice invoice = invoices.get(invoiceId);
            if (invoice == null) {
                throw new RuntimeException("发票不存在");
            }
            
            invoice.setReceivedAmount(invoice.getReceivedAmount().add(paymentAmount));
            invoice.setUpdatedAt(LocalDateTime.now());
            
            // 更新付款状态
            if (invoice.getReceivedAmount().compareTo(invoice.getAmount()) >= 0) {
                invoice.setPaymentStatus("PAID");
                invoice.setStatus("PAID");
            } else {
                invoice.setPaymentStatus("PARTIAL");
            }
            
            log.info("收款确认成功");
            return true;
        } catch (Exception e) {
            log.error("确认收款失败", e);
            throw new RuntimeException("确认收款失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> executeSalesToReceiptWorkflow(Long customerId, BigDecimal opportunityAmount, BigDecimal contractAmount) {
        log.info("执行销售到收款流程，customerId={}, opportunityAmount={}, contractAmount={}", customerId, opportunityAmount, contractAmount);
        
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 1. 创建销售机会
            SalesOpportunity opportunity = new SalesOpportunity();
            opportunity.setCustomerId(customerId);
            opportunity.setOpportunityName("销售机会-" + customerId);
            opportunity.setAmount(opportunityAmount);
            opportunity.setWinProbability(BigDecimal.valueOf(80));
            opportunity.setExpectedCloseDate(LocalDateTime.now().plusDays(30));
            opportunity.setSource("网站");
            opportunity.setOwnerId(1L);
            opportunity.setCreatedBy(1L);
            
            createSalesOpportunity(opportunity);
            result.put("opportunity", opportunity);
            
            // 2. 更新机会状态为已确认
            updateSalesOpportunityStatus(opportunity.getId(), "QUALIFIED");
            
            // 3. 创建合同
            Contract contract = new Contract();
            contract.setOpportunityId(opportunity.getId());
            contract.setCustomerId(customerId);
            contract.setContractName("销售合同-" + opportunity.getId());
            contract.setContractType("SALES");
            contract.setAmount(contractAmount);
            contract.setCurrency("CNY");
            contract.setStartDate(java.time.LocalDate.now());
            contract.setEndDate(java.time.LocalDate.now().plusMonths(12));
            contract.setPaymentTerms("30天");
            contract.setOwnerId(1L);
            contract.setCreatedBy(1L);
            
            createContract(contract);
            result.put("contract", contract);
            
            // 4. 合同审批
            approveContract(contract.getId(), 2L, true, "同意");
            
            // 5. 创建项目
            Project project = new Project();
            project.setContractId(contract.getId());
            project.setProjectName("项目-" + contract.getId());
            project.setDescription("项目描述");
            project.setBudget(contractAmount);
            project.setStartDate(java.time.LocalDate.now());
            project.setEndDate(java.time.LocalDate.now().plusMonths(6));
            project.setManagerId(1L);
            project.setCreatedBy(1L);
            
            createProject(project);
            result.put("project", project);
            
            // 6. 更新项目进度
            updateProjectProgress(project.getId(), BigDecimal.valueOf(50));
            
            // 7. 生成发票
            Invoice invoice = generateInvoice(project.getId(), contractAmount);
            result.put("invoice", invoice);
            
            // 8. 确认收款
            confirmPayment(invoice.getId(), contractAmount, "银行转账");
            
            result.put("success", true);
            result.put("message", "销售到收款流程执行成功");
            
            log.info("销售到收款流程执行完成");
            return result;
        } catch (Exception e) {
            log.error("执行销售到收款流程失败", e);
            throw new RuntimeException("执行销售到收款流程失败", e);
        }
    }

    // ==================== 采购到付款流程 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createPurchaseRequest(PurchaseRequest request) {
        log.info("创建采购申请，申请信息：{}", request);
        
        try {
            request.setId(purchaseRequestIdCounter++);
            request.setRequestNo("PR" + String.format("%06d", request.getId()));
            request.setCreatedAt(LocalDateTime.now());
            request.setUpdatedAt(LocalDateTime.now());
            
            if (request.getStatus() == null) {
                request.setStatus("DRAFT");
            }
            if (request.getApprovalStatus() == null) {
                request.setApprovalStatus("PENDING");
            }
            
            purchaseRequests.put(request.getId(), request);
            
            log.info("采购申请创建成功，ID={}", request.getId());
            return true;
        } catch (Exception e) {
            log.error("创建采购申请失败", e);
            throw new RuntimeException("创建采购申请失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approvePurchaseRequest(Long requestId, Long approverId, boolean approved, String comments) {
        log.info("采购申请审批，requestId={}, approverId={}, approved={}", requestId, approverId, approved);
        
        try {
            PurchaseRequest request = purchaseRequests.get(requestId);
            if (request == null) {
                throw new RuntimeException("采购申请不存在");
            }
            
            // 创建审批记录
            Approval approval = new Approval();
            approval.setId(approvalIdCounter++);
            approval.setBusinessType("PURCHASE_REQUEST");
            approval.setBusinessId(requestId);
            approval.setApproverId(approverId);
            approval.setApprovalLevel(1);
            approval.setResult(approved ? "APPROVED" : "REJECTED");
            approval.setComments(comments);
            approval.setApprovedAt(LocalDateTime.now());
            approval.setCreatedAt(LocalDateTime.now());
            approval.setUpdatedAt(LocalDateTime.now());
            
            approvals.put(approval.getId(), approval);
            
            // 更新申请状态
            if (approved) {
                request.setApprovalStatus("APPROVED");
                request.setStatus("APPROVED");
            } else {
                request.setApprovalStatus("REJECTED");
                request.setStatus("REJECTED");
            }
            request.setUpdatedAt(LocalDateTime.now());
            
            log.info("采购申请审批完成");
            return true;
        } catch (Exception e) {
            log.error("采购申请审批失败", e);
            throw new RuntimeException("采购申请审批失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean selectSupplier(Long requestId, Long supplierId, BigDecimal bidAmount) {
        log.info("选择供应商，requestId={}, supplierId={}, bidAmount={}", requestId, supplierId, bidAmount);
        
        try {
            PurchaseRequest request = purchaseRequests.get(requestId);
            if (request == null) {
                throw new RuntimeException("采购申请不存在");
            }
            
            request.setStatus("PROCESSING");
            request.setUpdatedAt(LocalDateTime.now());
            
            log.info("供应商选择完成");
            return true;
        } catch (Exception e) {
            log.error("选择供应商失败", e);
            throw new RuntimeException("选择供应商失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createReceipt(Long requestId, Integer receivedQuantity, String qualityStatus) {
        log.info("创建收货记录，requestId={}, receivedQuantity={}, qualityStatus={}", requestId, receivedQuantity, qualityStatus);
        
        try {
            PurchaseRequest request = purchaseRequests.get(requestId);
            if (request == null) {
                throw new RuntimeException("采购申请不存在");
            }
            
            request.setStatus("COMPLETED");
            request.setUpdatedAt(LocalDateTime.now());
            
            log.info("收货记录创建完成");
            return true;
        } catch (Exception e) {
            log.error("创建收货记录失败", e);
            throw new RuntimeException("创建收货记录失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processSupplierInvoice(Long receiptId, BigDecimal invoiceAmount, String invoiceNumber) {
        log.info("处理供应商发票，receiptId={}, invoiceAmount={}, invoiceNumber={}", receiptId, invoiceAmount, invoiceNumber);
        
        try {
            // 创建供应商发票记录
            Invoice invoice = new Invoice();
            invoice.setId(invoiceIdCounter++);
            invoice.setInvoiceNo(invoiceNumber);
            invoice.setInvoiceType("PURCHASE");
            invoice.setAmount(invoiceAmount);
            invoice.setReceivedAmount(BigDecimal.ZERO);
            invoice.setInvoiceDate(java.time.LocalDate.now());
            invoice.setDueDate(java.time.LocalDate.now().plusDays(30));
            invoice.setStatus("ISSUED");
            invoice.setPaymentStatus("UNPAID");
            invoice.setCreatedAt(LocalDateTime.now());
            invoice.setUpdatedAt(LocalDateTime.now());
            
            invoices.put(invoice.getId(), invoice);
            
            log.info("供应商发票处理完成");
            return true;
        } catch (Exception e) {
            log.error("处理供应商发票失败", e);
            throw new RuntimeException("处理供应商发票失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approvePayment(Long invoiceId, Long approverId, boolean approved, String comments) {
        log.info("付款审批，invoiceId={}, approverId={}, approved={}", invoiceId, approverId, approved);
        
        try {
            Invoice invoice = invoices.get(invoiceId);
            if (invoice == null) {
                throw new RuntimeException("发票不存在");
            }
            
            // 创建审批记录
            Approval approval = new Approval();
            approval.setId(approvalIdCounter++);
            approval.setBusinessType("INVOICE");
            approval.setBusinessId(invoiceId);
            approval.setApproverId(approverId);
            approval.setApprovalLevel(1);
            approval.setResult(approved ? "APPROVED" : "REJECTED");
            approval.setComments(comments);
            approval.setApprovedAt(LocalDateTime.now());
            approval.setCreatedAt(LocalDateTime.now());
            approval.setUpdatedAt(LocalDateTime.now());
            
            approvals.put(approval.getId(), approval);
            
            log.info("付款审批完成");
            return true;
        } catch (Exception e) {
            log.error("付款审批失败", e);
            throw new RuntimeException("付款审批失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean executePayment(Long invoiceId, BigDecimal paymentAmount, String paymentMethod) {
        log.info("执行付款，invoiceId={}, paymentAmount={}, paymentMethod={}", invoiceId, paymentAmount, paymentMethod);
        
        try {
            Invoice invoice = invoices.get(invoiceId);
            if (invoice == null) {
                throw new RuntimeException("发票不存在");
            }
            
            invoice.setReceivedAmount(invoice.getReceivedAmount().add(paymentAmount));
            invoice.setUpdatedAt(LocalDateTime.now());
            
            // 更新付款状态
            if (invoice.getReceivedAmount().compareTo(invoice.getAmount()) >= 0) {
                invoice.setPaymentStatus("PAID");
                invoice.setStatus("PAID");
            } else {
                invoice.setPaymentStatus("PARTIAL");
            }
            
            log.info("付款执行完成");
            return true;
        } catch (Exception e) {
            log.error("执行付款失败", e);
            throw new RuntimeException("执行付款失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> executePurchaseToPaymentWorkflow(Long departmentId, String itemName, Integer quantity, BigDecimal estimatedAmount) {
        log.info("执行采购到付款流程，departmentId={}, itemName={}, quantity={}, estimatedAmount={}", departmentId, itemName, quantity, estimatedAmount);
        
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 1. 创建采购申请
            PurchaseRequest request = new PurchaseRequest();
            request.setDepartmentId(departmentId);
            request.setRequesterId(1L);
            request.setItemName(itemName);
            request.setItemDescription("采购物品描述");
            request.setSpecification("规格型号");
            request.setQuantity(quantity);
            request.setUnit("个");
            request.setEstimatedUnitPrice(estimatedAmount.divide(BigDecimal.valueOf(quantity), 2, BigDecimal.ROUND_HALF_UP));
            request.setEstimatedTotalAmount(estimatedAmount);
            request.setRequiredDate(LocalDateTime.now().plusDays(7));
            request.setReason("业务需要");
            
            createPurchaseRequest(request);
            result.put("purchaseRequest", request);
            
            // 2. 采购申请审批
            approvePurchaseRequest(request.getId(), 2L, true, "同意采购");
            
            // 3. 选择供应商
            selectSupplier(request.getId(), 1L, estimatedAmount);
            
            // 4. 创建收货记录
            createReceipt(request.getId(), quantity, "PASS");
            
            // 5. 处理供应商发票
            processSupplierInvoice(request.getId(), estimatedAmount, "SUP" + String.format("%06d", request.getId()));
            
            // 6. 付款审批
            approvePayment(request.getId(), 3L, true, "同意付款");
            
            // 7. 执行付款
            executePayment(request.getId(), estimatedAmount, "银行转账");
            
            result.put("success", true);
            result.put("message", "采购到付款流程执行成功");
            
            log.info("采购到付款流程执行完成");
            return result;
        } catch (Exception e) {
            log.error("执行采购到付款流程失败", e);
            throw new RuntimeException("执行采购到付款流程失败", e);
        }
    }

    // ==================== 费用报销流程 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createExpenseRequest(ExpenseRequest request) {
        log.info("创建费用报销申请，申请信息：{}", request);
        
        try {
            request.setId(expenseRequestIdCounter++);
            request.setRequestNo("ER" + String.format("%06d", request.getId()));
            request.setCreatedAt(LocalDateTime.now());
            request.setUpdatedAt(LocalDateTime.now());
            
            if (request.getStatus() == null) {
                request.setStatus("DRAFT");
            }
            if (request.getApprovalStatus() == null) {
                request.setApprovalStatus("PENDING");
            }
            
            expenseRequests.put(request.getId(), request);
            
            log.info("费用报销申请创建成功，ID={}", request.getId());
            return true;
        } catch (Exception e) {
            log.error("创建费用报销申请失败", e);
            throw new RuntimeException("创建费用报销申请失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean uploadExpenseAttachment(Long requestId, String attachmentType, String attachmentUrl) {
        log.info("上传报销单据，requestId={}, attachmentType={}, attachmentUrl={}", requestId, attachmentType, attachmentUrl);
        
        try {
            ExpenseRequest request = expenseRequests.get(requestId);
            if (request == null) {
                throw new RuntimeException("费用报销申请不存在");
            }
            
            request.setStatus("SUBMITTED");
            request.setUpdatedAt(LocalDateTime.now());
            
            log.info("报销单据上传完成");
            return true;
        } catch (Exception e) {
            log.error("上传报销单据失败", e);
            throw new RuntimeException("上传报销单据失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean managerApprove(Long requestId, Long managerId, boolean approved, String comments) {
        log.info("部门经理审批，requestId={}, managerId={}, approved={}", requestId, managerId, approved);
        
        try {
            ExpenseRequest request = expenseRequests.get(requestId);
            if (request == null) {
                throw new RuntimeException("费用报销申请不存在");
            }
            
            // 创建审批记录
            Approval approval = new Approval();
            approval.setId(approvalIdCounter++);
            approval.setBusinessType("EXPENSE_REQUEST");
            approval.setBusinessId(requestId);
            approval.setApproverId(managerId);
            approval.setApprovalLevel(1);
            approval.setResult(approved ? "APPROVED" : "REJECTED");
            approval.setComments(comments);
            approval.setApprovedAt(LocalDateTime.now());
            approval.setCreatedAt(LocalDateTime.now());
            approval.setUpdatedAt(LocalDateTime.now());
            
            approvals.put(approval.getId(), approval);
            
            // 更新申请状态
            if (approved) {
                request.setStatus("MANAGER_APPROVED");
                request.setManagerId(managerId);
            } else {
                request.setStatus("REJECTED");
                request.setApprovalStatus("REJECTED");
            }
            request.setUpdatedAt(LocalDateTime.now());
            
            log.info("部门经理审批完成");
            return true;
        } catch (Exception e) {
            log.error("部门经理审批失败", e);
            throw new RuntimeException("部门经理审批失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean financeReview(Long requestId, Long financeId, boolean approved, String comments) {
        log.info("财务审核，requestId={}, financeId={}, approved={}", requestId, financeId, approved);
        
        try {
            ExpenseRequest request = expenseRequests.get(requestId);
            if (request == null) {
                throw new RuntimeException("费用报销申请不存在");
            }
            
            // 创建审批记录
            Approval approval = new Approval();
            approval.setId(approvalIdCounter++);
            approval.setBusinessType("EXPENSE_REQUEST");
            approval.setBusinessId(requestId);
            approval.setApproverId(financeId);
            approval.setApprovalLevel(2);
            approval.setResult(approved ? "APPROVED" : "REJECTED");
            approval.setComments(comments);
            approval.setApprovedAt(LocalDateTime.now());
            approval.setCreatedAt(LocalDateTime.now());
            approval.setUpdatedAt(LocalDateTime.now());
            
            approvals.put(approval.getId(), approval);
            
            // 更新申请状态
            if (approved) {
                request.setStatus("FINANCE_REVIEWED");
                request.setFinanceId(financeId);
            } else {
                request.setStatus("REJECTED");
                request.setApprovalStatus("REJECTED");
            }
            request.setUpdatedAt(LocalDateTime.now());
            
            log.info("财务审核完成");
            return true;
        } catch (Exception e) {
            log.error("财务审核失败", e);
            throw new RuntimeException("财务审核失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean ceoApprove(Long requestId, Long ceoId, boolean approved, String comments) {
        log.info("CEO审批，requestId={}, ceoId={}, approved={}", requestId, ceoId, approved);
        
        try {
            ExpenseRequest request = expenseRequests.get(requestId);
            if (request == null) {
                throw new RuntimeException("费用报销申请不存在");
            }
            
            // 创建审批记录
            Approval approval = new Approval();
            approval.setId(approvalIdCounter++);
            approval.setBusinessType("EXPENSE_REQUEST");
            approval.setBusinessId(requestId);
            approval.setApproverId(ceoId);
            approval.setApprovalLevel(3);
            approval.setResult(approved ? "APPROVED" : "REJECTED");
            approval.setComments(comments);
            approval.setApprovedAt(LocalDateTime.now());
            approval.setCreatedAt(LocalDateTime.now());
            approval.setUpdatedAt(LocalDateTime.now());
            
            approvals.put(approval.getId(), approval);
            
            // 更新申请状态
            if (approved) {
                request.setStatus("CEO_APPROVED");
                request.setCeoId(ceoId);
            } else {
                request.setStatus("REJECTED");
                request.setApprovalStatus("REJECTED");
            }
            request.setUpdatedAt(LocalDateTime.now());
            
            log.info("CEO审批完成");
            return true;
        } catch (Exception e) {
            log.error("CEO审批失败", e);
            throw new RuntimeException("CEO审批失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean generateAccountingVoucher(Long requestId) {
        log.info("生成会计凭证，requestId={}", requestId);
        
        try {
            ExpenseRequest request = expenseRequests.get(requestId);
            if (request == null) {
                throw new RuntimeException("费用报销申请不存在");
            }
            
            // 模拟生成会计凭证
            log.info("会计凭证生成完成");
            return true;
        } catch (Exception e) {
            log.error("生成会计凭证失败", e);
            throw new RuntimeException("生成会计凭证失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean executeExpensePayment(Long requestId, BigDecimal paymentAmount, String paymentMethod) {
        log.info("执行费用付款，requestId={}, paymentAmount={}, paymentMethod={}", requestId, paymentAmount, paymentMethod);
        
        try {
            ExpenseRequest request = expenseRequests.get(requestId);
            if (request == null) {
                throw new RuntimeException("费用报销申请不存在");
            }
            
            request.setStatus("PAID");
            request.setUpdatedAt(LocalDateTime.now());
            
            log.info("费用付款执行完成");
            return true;
        } catch (Exception e) {
            log.error("执行费用付款失败", e);
            throw new RuntimeException("执行费用付款失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> executeExpenseReimbursementWorkflow(Long employeeId, String expenseType, BigDecimal amount, String description) {
        log.info("执行费用报销流程，employeeId={}, expenseType={}, amount={}, description={}", employeeId, expenseType, amount, description);
        
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 1. 创建费用报销申请
            ExpenseRequest request = new ExpenseRequest();
            request.setEmployeeId(employeeId);
            request.setExpenseType(expenseType);
            request.setAmount(amount);
            request.setDescription(description);
            request.setExpenseDate(java.time.LocalDate.now());
            
            createExpenseRequest(request);
            result.put("expenseRequest", request);
            
            // 2. 上传报销单据
            uploadExpenseAttachment(request.getId(), "INVOICE", "attachment_url");
            
            // 3. 部门经理审批
            managerApprove(request.getId(), 2L, true, "同意报销");
            
            // 4. 财务审核
            financeReview(request.getId(), 3L, true, "审核通过");
            
            // 5. CEO审批（大额费用）
            if (amount.compareTo(BigDecimal.valueOf(10000)) > 0) {
                ceoApprove(request.getId(), 4L, true, "同意报销");
            }
            
            // 6. 生成会计凭证
            generateAccountingVoucher(request.getId());
            
            // 7. 执行付款
            executeExpensePayment(request.getId(), amount, "银行转账");
            
            result.put("success", true);
            result.put("message", "费用报销流程执行成功");
            
            log.info("费用报销流程执行完成");
            return result;
        } catch (Exception e) {
            log.error("执行费用报销流程失败", e);
            throw new RuntimeException("执行费用报销流程失败", e);
        }
    }

    // ==================== 流程查询 ====================

    @Override
    public List<SalesOpportunity> getSalesOpportunities(Long customerId, String status) {
        log.info("获取销售机会列表，customerId={}, status={}", customerId, status);
        
        try {
            List<SalesOpportunity> result = new ArrayList<>();
            for (SalesOpportunity opportunity : salesOpportunities.values()) {
                if ((customerId == null || opportunity.getCustomerId().equals(customerId)) &&
                    (status == null || status.equals(opportunity.getStatus()))) {
                    result.add(opportunity);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("获取销售机会列表失败", e);
            throw new RuntimeException("获取销售机会列表失败", e);
        }
    }

    @Override
    public List<Contract> getContracts(Long customerId, String status) {
        log.info("获取合同列表，customerId={}, status={}", customerId, status);
        
        try {
            List<Contract> result = new ArrayList<>();
            for (Contract contract : contracts.values()) {
                if ((customerId == null || contract.getCustomerId().equals(customerId)) &&
                    (status == null || status.equals(contract.getStatus()))) {
                    result.add(contract);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("获取合同列表失败", e);
            throw new RuntimeException("获取合同列表失败", e);
        }
    }

    @Override
    public List<Project> getProjects(Long contractId, String status) {
        log.info("获取项目列表，contractId={}, status={}", contractId, status);
        
        try {
            List<Project> result = new ArrayList<>();
            for (Project project : projects.values()) {
                if ((contractId == null || project.getContractId().equals(contractId)) &&
                    (status == null || status.equals(project.getStatus()))) {
                    result.add(project);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("获取项目列表失败", e);
            throw new RuntimeException("获取项目列表失败", e);
        }
    }

    @Override
    public List<Invoice> getInvoices(Long projectId, String status) {
        log.info("获取发票列表，projectId={}, status={}", projectId, status);
        
        try {
            List<Invoice> result = new ArrayList<>();
            for (Invoice invoice : invoices.values()) {
                if ((projectId == null || invoice.getProjectId().equals(projectId)) &&
                    (status == null || status.equals(invoice.getStatus()))) {
                    result.add(invoice);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("获取发票列表失败", e);
            throw new RuntimeException("获取发票列表失败", e);
        }
    }

    @Override
    public List<PurchaseRequest> getPurchaseRequests(Long departmentId, String status) {
        log.info("获取采购申请列表，departmentId={}, status={}", departmentId, status);
        
        try {
            List<PurchaseRequest> result = new ArrayList<>();
            for (PurchaseRequest request : purchaseRequests.values()) {
                if ((departmentId == null || request.getDepartmentId().equals(departmentId)) &&
                    (status == null || status.equals(request.getStatus()))) {
                    result.add(request);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("获取采购申请列表失败", e);
            throw new RuntimeException("获取采购申请列表失败", e);
        }
    }

    @Override
    public List<ExpenseRequest> getExpenseRequests(Long employeeId, String status) {
        log.info("获取费用报销申请列表，employeeId={}, status={}", employeeId, status);
        
        try {
            List<ExpenseRequest> result = new ArrayList<>();
            for (ExpenseRequest request : expenseRequests.values()) {
                if ((employeeId == null || request.getEmployeeId().equals(employeeId)) &&
                    (status == null || status.equals(request.getStatus()))) {
                    result.add(request);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("获取费用报销申请列表失败", e);
            throw new RuntimeException("获取费用报销申请列表失败", e);
        }
    }

    @Override
    public List<Approval> getApprovals(String businessType, Long businessId) {
        log.info("获取审批记录，businessType={}, businessId={}", businessType, businessId);
        
        try {
            List<Approval> result = new ArrayList<>();
            for (Approval approval : approvals.values()) {
                if ((businessType == null || businessType.equals(approval.getBusinessType())) &&
                    (businessId == null || businessId.equals(approval.getBusinessId()))) {
                    result.add(approval);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("获取审批记录失败", e);
            throw new RuntimeException("获取审批记录失败", e);
        }
    }

    @Override
    public Map<String, Object> getWorkflowStatistics() {
        log.info("获取流程统计信息");
        
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 销售机会统计
            statistics.put("totalOpportunities", salesOpportunities.size());
            statistics.put("qualifiedOpportunities", getSalesOpportunities(null, "QUALIFIED").size());
            statistics.put("closedWonOpportunities", getSalesOpportunities(null, "CLOSED_WON").size());
            
            // 合同统计
            statistics.put("totalContracts", contracts.size());
            statistics.put("approvedContracts", getContracts(null, "APPROVED").size());
            statistics.put("executingContracts", getContracts(null, "EXECUTING").size());
            
            // 项目统计
            statistics.put("totalProjects", projects.size());
            statistics.put("executingProjects", getProjects(null, "EXECUTING").size());
            statistics.put("completedProjects", getProjects(null, "COMPLETED").size());
            
            // 发票统计
            statistics.put("totalInvoices", invoices.size());
            statistics.put("paidInvoices", getInvoices(null, "PAID").size());
            statistics.put("unpaidInvoices", getInvoices(null, "ISSUED").size());
            
            // 采购申请统计
            statistics.put("totalPurchaseRequests", purchaseRequests.size());
            statistics.put("approvedPurchaseRequests", getPurchaseRequests(null, "APPROVED").size());
            statistics.put("completedPurchaseRequests", getPurchaseRequests(null, "COMPLETED").size());
            
            // 费用报销统计
            statistics.put("totalExpenseRequests", expenseRequests.size());
            statistics.put("paidExpenseRequests", getExpenseRequests(null, "PAID").size());
            statistics.put("pendingExpenseRequests", getExpenseRequests(null, "SUBMITTED").size());
            
            log.info("流程统计信息获取完成");
            return statistics;
        } catch (Exception e) {
            log.error("获取流程统计信息失败", e);
            throw new RuntimeException("获取流程统计信息失败", e);
        }
    }
} 