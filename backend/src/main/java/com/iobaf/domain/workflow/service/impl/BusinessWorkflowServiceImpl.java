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
 * 实现销售到收款、采购到付款、费用报销等核心业务流程
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
        log.info("创建销售机会，客户ID={}，金额={}", opportunity.getCustomerId(), opportunity.getAmount());
        
        try {
            opportunity.setId(salesOpportunityIdCounter++);
            opportunity.setOpportunityNo("OPP" + String.format("%06d", opportunity.getId()));
            opportunity.setStatus("LEAD");
            opportunity.setCreatedAt(LocalDateTime.now());
            opportunity.setUpdatedAt(LocalDateTime.now());
            
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
        log.info("更新销售机会状态，ID={}，新状态={}", opportunityId, status);
        
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
        log.info("创建合同，客户ID={}，金额={}", contract.getCustomerId(), contract.getAmount());
        
        try {
            contract.setId(contractIdCounter++);
            contract.setContractNo("CON" + String.format("%06d", contract.getId()));
            contract.setStatus("DRAFT");
            contract.setApprovalStatus("PENDING");
            contract.setCreatedAt(LocalDateTime.now());
            contract.setUpdatedAt(LocalDateTime.now());
            
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
        log.info("合同审批，合同ID={}，审批人ID={}，结果={}", contractId, approverId, approved);
        
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
            contract.setApprovalStatus(approved ? "APPROVED" : "REJECTED");
            if (approved) {
                contract.setStatus("APPROVED");
            }
            contract.setUpdatedAt(LocalDateTime.now());
            
            log.info("合同审批完成，结果={}", approved);
            return true;
        } catch (Exception e) {
            log.error("合同审批失败", e);
            throw new RuntimeException("合同审批失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createProject(Project project) {
        log.info("创建项目，合同ID={}，预算={}", project.getContractId(), project.getBudget());
        
        try {
            project.setId(projectIdCounter++);
            project.setProjectNo("PRJ" + String.format("%06d", project.getId()));
            project.setStatus("PLANNING");
            project.setProgress(BigDecimal.ZERO);
            project.setCreatedAt(LocalDateTime.now());
            project.setUpdatedAt(LocalDateTime.now());
            
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
        log.info("更新项目进度，项目ID={}，进度={}%", projectId, progress);
        
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
        log.info("生成发票，项目ID={}，金额={}", projectId, amount);
        
        try {
            Project project = projects.get(projectId);
            if (project == null) {
                throw new RuntimeException("项目不存在");
            }
            
            Invoice invoice = new Invoice();
            invoice.setId(invoiceIdCounter++);
            invoice.setInvoiceNo("INV" + String.format("%06d", invoice.getId()));
            invoice.setProjectId(projectId);
            invoice.setInvoiceType("SALES");
            invoice.setAmount(amount);
            invoice.setReceivedAmount(BigDecimal.ZERO);
            invoice.setStatus("ISSUED");
            invoice.setPaymentStatus("UNPAID");
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
        log.info("确认收款，发票ID={}，收款金额={}，收款方式={}", invoiceId, paymentAmount, paymentMethod);
        
        try {
            Invoice invoice = invoices.get(invoiceId);
            if (invoice == null) {
                throw new RuntimeException("发票不存在");
            }
            
            BigDecimal newReceivedAmount = invoice.getReceivedAmount().add(paymentAmount);
            invoice.setReceivedAmount(newReceivedAmount);
            invoice.setUpdatedAt(LocalDateTime.now());
            
            // 更新付款状态
            if (newReceivedAmount.compareTo(invoice.getAmount()) >= 0) {
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
        log.info("执行销售到收款流程，客户ID={}，机会金额={}，合同金额={}", customerId, opportunityAmount, contractAmount);
        
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 1. 创建销售机会
            SalesOpportunity opportunity = new SalesOpportunity();
            opportunity.setCustomerId(customerId);
            opportunity.setOpportunityName("销售机会-" + customerId);
            opportunity.setAmount(opportunityAmount);
            opportunity.setWinProbability(BigDecimal.valueOf(80));
            opportunity.setOwnerId(1L);
            opportunity.setCreatedBy(1L);
            
            boolean opportunityCreated = createSalesOpportunity(opportunity);
            result.put("opportunityCreated", opportunityCreated);
            result.put("opportunityId", opportunity.getId());
            
            // 2. 更新机会状态为已确认
            updateSalesOpportunityStatus(opportunity.getId(), "QUALIFIED");
            
            // 3. 创建合同
            Contract contract = new Contract();
            contract.setOpportunityId(opportunity.getId());
            contract.setCustomerId(customerId);
            contract.setContractName("合同-" + opportunity.getId());
            contract.setContractType("SALES");
            contract.setAmount(contractAmount);
            contract.setOwnerId(1L);
            contract.setCreatedBy(1L);
            
            boolean contractCreated = createContract(contract);
            result.put("contractCreated", contractCreated);
            result.put("contractId", contract.getId());
            
            // 4. 合同审批
            boolean contractApproved = approveContract(contract.getId(), 2L, true, "同意");
            result.put("contractApproved", contractApproved);
            
            // 5. 创建项目
            Project project = new Project();
            project.setContractId(contract.getId());
            project.setProjectName("项目-" + contract.getId());
            project.setBudget(contractAmount);
            project.setManagerId(1L);
            project.setCreatedBy(1L);
            
            boolean projectCreated = createProject(project);
            result.put("projectCreated", projectCreated);
            result.put("projectId", project.getId());
            
            // 6. 更新项目进度
            updateProjectProgress(project.getId(), BigDecimal.valueOf(50));
            
            // 7. 生成发票
            Invoice invoice = generateInvoice(project.getId(), contractAmount);
            result.put("invoiceGenerated", invoice != null);
            result.put("invoiceId", invoice.getId());
            
            // 8. 确认收款
            boolean paymentConfirmed = confirmPayment(invoice.getId(), contractAmount, "银行转账");
            result.put("paymentConfirmed", paymentConfirmed);
            
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
        log.info("创建采购申请，部门ID={}，物品名称={}，数量={}", request.getDepartmentId(), request.getItemName(), request.getQuantity());
        
        try {
            request.setId(purchaseRequestIdCounter++);
            request.setRequestNo("PR" + String.format("%06d", request.getId()));
            request.setStatus("DRAFT");
            request.setApprovalStatus("PENDING");
            request.setCreatedAt(LocalDateTime.now());
            request.setUpdatedAt(LocalDateTime.now());
            
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
        log.info("采购申请审批，申请ID={}，审批人ID={}，结果={}", requestId, approverId, approved);
        
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
            request.setApprovalStatus(approved ? "APPROVED" : "REJECTED");
            if (approved) {
                request.setStatus("APPROVED");
            }
            request.setUpdatedAt(LocalDateTime.now());
            
            log.info("采购申请审批完成，结果={}", approved);
            return true;
        } catch (Exception e) {
            log.error("采购申请审批失败", e);
            throw new RuntimeException("采购申请审批失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean selectSupplier(Long requestId, Long supplierId, BigDecimal bidAmount) {
        log.info("选择供应商，申请ID={}，供应商ID={}，投标金额={}", requestId, supplierId, bidAmount);
        
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
        log.info("创建收货记录，申请ID={}，收货数量={}，质量状态={}", requestId, receivedQuantity, qualityStatus);
        
        try {
            PurchaseRequest request = purchaseRequests.get(requestId);
            if (request == null) {
                throw new RuntimeException("采购申请不存在");
            }
            
            request.setStatus("COMPLETED");
            request.setUpdatedAt(LocalDateTime.now());
            
            log.info("收货记录创建成功");
            return true;
        } catch (Exception e) {
            log.error("创建收货记录失败", e);
            throw new RuntimeException("创建收货记录失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processSupplierInvoice(Long receiptId, BigDecimal invoiceAmount, String invoiceNumber) {
        log.info("处理供应商发票，收货记录ID={}，发票金额={}，发票号码={}", receiptId, invoiceAmount, invoiceNumber);
        
        try {
            // 这里应该创建应付账款记录
            log.info("供应商发票处理成功");
            return true;
        } catch (Exception e) {
            log.error("处理供应商发票失败", e);
            throw new RuntimeException("处理供应商发票失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approvePayment(Long invoiceId, Long approverId, boolean approved, String comments) {
        log.info("付款审批，发票ID={}，审批人ID={}，结果={}", invoiceId, approverId, approved);
        
        try {
            // 创建审批记录
            Approval approval = new Approval();
            approval.setId(approvalIdCounter++);
            approval.setBusinessType("PAYMENT");
            approval.setBusinessId(invoiceId);
            approval.setApproverId(approverId);
            approval.setApprovalLevel(1);
            approval.setResult(approved ? "APPROVED" : "REJECTED");
            approval.setComments(comments);
            approval.setApprovedAt(LocalDateTime.now());
            approval.setCreatedAt(LocalDateTime.now());
            approval.setUpdatedAt(LocalDateTime.now());
            
            approvals.put(approval.getId(), approval);
            
            log.info("付款审批完成，结果={}", approved);
            return true;
        } catch (Exception e) {
            log.error("付款审批失败", e);
            throw new RuntimeException("付款审批失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean executePayment(Long invoiceId, BigDecimal paymentAmount, String paymentMethod) {
        log.info("执行付款，发票ID={}，付款金额={}，付款方式={}", invoiceId, paymentAmount, paymentMethod);
        
        try {
            log.info("付款执行成功");
            return true;
        } catch (Exception e) {
            log.error("执行付款失败", e);
            throw new RuntimeException("执行付款失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> executePurchaseToPaymentWorkflow(Long departmentId, String itemName, Integer quantity, BigDecimal estimatedAmount) {
        log.info("执行采购到付款流程，部门ID={}，物品名称={}，数量={}，预估金额={}", departmentId, itemName, quantity, estimatedAmount);
        
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 1. 创建采购申请
            PurchaseRequest request = new PurchaseRequest();
            request.setDepartmentId(departmentId);
            request.setRequesterId(1L);
            request.setItemName(itemName);
            request.setQuantity(quantity);
            request.setEstimatedTotalAmount(estimatedAmount);
            request.setReason("业务需要");
            
            boolean requestCreated = createPurchaseRequest(request);
            result.put("requestCreated", requestCreated);
            result.put("requestId", request.getId());
            
            // 2. 采购申请审批
            boolean requestApproved = approvePurchaseRequest(request.getId(), 2L, true, "同意采购");
            result.put("requestApproved", requestApproved);
            
            // 3. 选择供应商
            boolean supplierSelected = selectSupplier(request.getId(), 1L, estimatedAmount);
            result.put("supplierSelected", supplierSelected);
            
            // 4. 创建收货记录
            boolean receiptCreated = createReceipt(request.getId(), quantity, "QUALIFIED");
            result.put("receiptCreated", receiptCreated);
            
            // 5. 处理供应商发票
            boolean invoiceProcessed = processSupplierInvoice(request.getId(), estimatedAmount, "INV001");
            result.put("invoiceProcessed", invoiceProcessed);
            
            // 6. 付款审批
            boolean paymentApproved = approvePayment(request.getId(), 3L, true, "同意付款");
            result.put("paymentApproved", paymentApproved);
            
            // 7. 执行付款
            boolean paymentExecuted = executePayment(request.getId(), estimatedAmount, "银行转账");
            result.put("paymentExecuted", paymentExecuted);
            
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
        log.info("创建费用报销申请，员工ID={}，费用类型={}，金额={}", request.getEmployeeId(), request.getExpenseType(), request.getAmount());
        
        try {
            request.setId(expenseRequestIdCounter++);
            request.setRequestNo("EXP" + String.format("%06d", request.getId()));
            request.setStatus("DRAFT");
            request.setApprovalStatus("PENDING");
            request.setCreatedAt(LocalDateTime.now());
            request.setUpdatedAt(LocalDateTime.now());
            
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
        log.info("上传报销单据，申请ID={}，附件类型={}，附件URL={}", requestId, attachmentType, attachmentUrl);
        
        try {
            ExpenseRequest request = expenseRequests.get(requestId);
            if (request == null) {
                throw new RuntimeException("费用报销申请不存在");
            }
            
            request.setStatus("SUBMITTED");
            request.setUpdatedAt(LocalDateTime.now());
            
            log.info("报销单据上传成功");
            return true;
        } catch (Exception e) {
            log.error("上传报销单据失败", e);
            throw new RuntimeException("上传报销单据失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean managerApprove(Long requestId, Long managerId, boolean approved, String comments) {
        log.info("部门经理审批，申请ID={}，经理ID={}，结果={}", requestId, managerId, approved);
        
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
            } else {
                request.setStatus("REJECTED");
            }
            request.setUpdatedAt(LocalDateTime.now());
            
            log.info("部门经理审批完成，结果={}", approved);
            return true;
        } catch (Exception e) {
            log.error("部门经理审批失败", e);
            throw new RuntimeException("部门经理审批失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean financeReview(Long requestId, Long financeId, boolean approved, String comments) {
        log.info("财务审核，申请ID={}，财务人员ID={}，结果={}", requestId, financeId, approved);
        
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
            } else {
                request.setStatus("REJECTED");
            }
            request.setUpdatedAt(LocalDateTime.now());
            
            log.info("财务审核完成，结果={}", approved);
            return true;
        } catch (Exception e) {
            log.error("财务审核失败", e);
            throw new RuntimeException("财务审核失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean ceoApprove(Long requestId, Long ceoId, boolean approved, String comments) {
        log.info("CEO审批，申请ID={}，CEO ID={}，结果={}", requestId, ceoId, approved);
        
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
            } else {
                request.setStatus("REJECTED");
            }
            request.setUpdatedAt(LocalDateTime.now());
            
            log.info("CEO审批完成，结果={}", approved);
            return true;
        } catch (Exception e) {
            log.error("CEO审批失败", e);
            throw new RuntimeException("CEO审批失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean generateAccountingVoucher(Long requestId) {
        log.info("生成会计凭证，申请ID={}", requestId);
        
        try {
            ExpenseRequest request = expenseRequests.get(requestId);
            if (request == null) {
                throw new RuntimeException("费用报销申请不存在");
            }
            
            log.info("会计凭证生成成功");
            return true;
        } catch (Exception e) {
            log.error("生成会计凭证失败", e);
            throw new RuntimeException("生成会计凭证失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean executeExpensePayment(Long requestId, BigDecimal paymentAmount, String paymentMethod) {
        log.info("执行费用付款，申请ID={}，付款金额={}，付款方式={}", requestId, paymentAmount, paymentMethod);
        
        try {
            ExpenseRequest request = expenseRequests.get(requestId);
            if (request == null) {
                throw new RuntimeException("费用报销申请不存在");
            }
            
            request.setStatus("PAID");
            request.setUpdatedAt(LocalDateTime.now());
            
            log.info("费用付款执行成功");
            return true;
        } catch (Exception e) {
            log.error("执行费用付款失败", e);
            throw new RuntimeException("执行费用付款失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> executeExpenseReimbursementWorkflow(Long employeeId, String expenseType, BigDecimal amount, String description) {
        log.info("执行费用报销流程，员工ID={}，费用类型={}，金额={}，描述={}", employeeId, expenseType, amount, description);
        
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 1. 创建费用报销申请
            ExpenseRequest request = new ExpenseRequest();
            request.setEmployeeId(employeeId);
            request.setExpenseType(expenseType);
            request.setAmount(amount);
            request.setDescription(description);
            request.setExpenseDate(LocalDateTime.now().toLocalDate());
            
            boolean requestCreated = createExpenseRequest(request);
            result.put("requestCreated", requestCreated);
            result.put("requestId", request.getId());
            
            // 2. 上传报销单据
            boolean attachmentUploaded = uploadExpenseAttachment(request.getId(), "INVOICE", "invoice_url");
            result.put("attachmentUploaded", attachmentUploaded);
            
            // 3. 部门经理审批
            boolean managerApproved = managerApprove(request.getId(), 2L, true, "同意报销");
            result.put("managerApproved", managerApproved);
            
            // 4. 财务审核
            boolean financeReviewed = financeReview(request.getId(), 3L, true, "审核通过");
            result.put("financeReviewed", financeReviewed);
            
            // 5. CEO审批（大额费用）
            boolean ceoApproved = ceoApprove(request.getId(), 4L, true, "同意");
            result.put("ceoApproved", ceoApproved);
            
            // 6. 生成会计凭证
            boolean voucherGenerated = generateAccountingVoucher(request.getId());
            result.put("voucherGenerated", voucherGenerated);
            
            // 7. 执行付款
            boolean paymentExecuted = executeExpensePayment(request.getId(), amount, "银行转账");
            result.put("paymentExecuted", paymentExecuted);
            
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
        log.info("查询销售机会列表，客户ID={}，状态={}", customerId, status);
        
        try {
            List<SalesOpportunity> result = new ArrayList<>();
            for (SalesOpportunity opportunity : salesOpportunities.values()) {
                if ((customerId == null || opportunity.getCustomerId().equals(customerId)) &&
                    (status == null || opportunity.getStatus().equals(status))) {
                    result.add(opportunity);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("查询销售机会列表失败", e);
            throw new RuntimeException("查询销售机会列表失败", e);
        }
    }

    @Override
    public List<Contract> getContracts(Long customerId, String status) {
        log.info("查询合同列表，客户ID={}，状态={}", customerId, status);
        
        try {
            List<Contract> result = new ArrayList<>();
            for (Contract contract : contracts.values()) {
                if ((customerId == null || contract.getCustomerId().equals(customerId)) &&
                    (status == null || contract.getStatus().equals(status))) {
                    result.add(contract);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("查询合同列表失败", e);
            throw new RuntimeException("查询合同列表失败", e);
        }
    }

    @Override
    public List<Project> getProjects(Long contractId, String status) {
        log.info("查询项目列表，合同ID={}，状态={}", contractId, status);
        
        try {
            List<Project> result = new ArrayList<>();
            for (Project project : projects.values()) {
                if ((contractId == null || project.getContractId().equals(contractId)) &&
                    (status == null || project.getStatus().equals(status))) {
                    result.add(project);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("查询项目列表失败", e);
            throw new RuntimeException("查询项目列表失败", e);
        }
    }

    @Override
    public List<Invoice> getInvoices(Long projectId, String status) {
        log.info("查询发票列表，项目ID={}，状态={}", projectId, status);
        
        try {
            List<Invoice> result = new ArrayList<>();
            for (Invoice invoice : invoices.values()) {
                if ((projectId == null || invoice.getProjectId().equals(projectId)) &&
                    (status == null || invoice.getStatus().equals(status))) {
                    result.add(invoice);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("查询发票列表失败", e);
            throw new RuntimeException("查询发票列表失败", e);
        }
    }

    @Override
    public List<PurchaseRequest> getPurchaseRequests(Long departmentId, String status) {
        log.info("查询采购申请列表，部门ID={}，状态={}", departmentId, status);
        
        try {
            List<PurchaseRequest> result = new ArrayList<>();
            for (PurchaseRequest request : purchaseRequests.values()) {
                if ((departmentId == null || request.getDepartmentId().equals(departmentId)) &&
                    (status == null || request.getStatus().equals(status))) {
                    result.add(request);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("查询采购申请列表失败", e);
            throw new RuntimeException("查询采购申请列表失败", e);
        }
    }

    @Override
    public List<ExpenseRequest> getExpenseRequests(Long employeeId, String status) {
        log.info("查询费用报销申请列表，员工ID={}，状态={}", employeeId, status);
        
        try {
            List<ExpenseRequest> result = new ArrayList<>();
            for (ExpenseRequest request : expenseRequests.values()) {
                if ((employeeId == null || request.getEmployeeId().equals(employeeId)) &&
                    (status == null || request.getStatus().equals(status))) {
                    result.add(request);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("查询费用报销申请列表失败", e);
            throw new RuntimeException("查询费用报销申请列表失败", e);
        }
    }

    @Override
    public List<Approval> getApprovals(String businessType, Long businessId) {
        log.info("查询审批记录，业务类型={}，业务ID={}", businessType, businessId);
        
        try {
            List<Approval> result = new ArrayList<>();
            for (Approval approval : approvals.values()) {
                if ((businessType == null || approval.getBusinessType().equals(businessType)) &&
                    (businessId == null || approval.getBusinessId().equals(businessId))) {
                    result.add(approval);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("查询审批记录失败", e);
            throw new RuntimeException("查询审批记录失败", e);
        }
    }

    @Override
    public Map<String, Object> getWorkflowStatistics() {
        log.info("获取流程统计信息");
        
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 销售机会统计
            statistics.put("totalOpportunities", salesOpportunities.size());
            statistics.put("wonOpportunities", salesOpportunities.values().stream()
                    .filter(o -> "CLOSED_WON".equals(o.getStatus())).count());
            
            // 合同统计
            statistics.put("totalContracts", contracts.size());
            statistics.put("approvedContracts", contracts.values().stream()
                    .filter(c -> "APPROVED".equals(c.getApprovalStatus())).count());
            
            // 项目统计
            statistics.put("totalProjects", projects.size());
            statistics.put("completedProjects", projects.values().stream()
                    .filter(p -> "COMPLETED".equals(p.getStatus())).count());
            
            // 发票统计
            statistics.put("totalInvoices", invoices.size());
            statistics.put("paidInvoices", invoices.values().stream()
                    .filter(i -> "PAID".equals(i.getPaymentStatus())).count());
            
            // 采购申请统计
            statistics.put("totalPurchaseRequests", purchaseRequests.size());
            statistics.put("approvedPurchaseRequests", purchaseRequests.values().stream()
                    .filter(p -> "APPROVED".equals(p.getApprovalStatus())).count());
            
            // 费用报销统计
            statistics.put("totalExpenseRequests", expenseRequests.size());
            statistics.put("paidExpenseRequests", expenseRequests.values().stream()
                    .filter(e -> "PAID".equals(e.getStatus())).count());
            
            log.info("流程统计信息获取完成");
            return statistics;
        } catch (Exception e) {
            log.error("获取流程统计信息失败", e);
            throw new RuntimeException("获取流程统计信息失败", e);
        }
    }
} 