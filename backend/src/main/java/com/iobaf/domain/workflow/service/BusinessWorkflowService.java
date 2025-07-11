package com.iobaf.domain.workflow.service;

import com.iobaf.domain.workflow.entity.SalesOpportunity;
import com.iobaf.domain.workflow.entity.Contract;
import com.iobaf.domain.workflow.entity.Project;
import com.iobaf.domain.workflow.entity.Invoice;
import com.iobaf.domain.workflow.entity.Payment;
import com.iobaf.domain.workflow.entity.PurchaseRequest;
import com.iobaf.domain.workflow.entity.Supplier;
import com.iobaf.domain.workflow.entity.Receipt;
import com.iobaf.domain.workflow.entity.ExpenseRequest;
import com.iobaf.domain.workflow.entity.Approval;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 核心业务流程服务接口
 * 实现销售到收款、采购到付款、费用报销等核心业务流程
 * 
 * @author iobaf
 * @since 2024-01-01
 */
public interface BusinessWorkflowService {

    // ==================== 销售到收款流程 ====================

    /**
     * 创建销售机会
     * 
     * @param opportunity 销售机会信息
     * @return 创建结果
     */
    boolean createSalesOpportunity(SalesOpportunity opportunity);

    /**
     * 更新销售机会状态
     * 
     * @param opportunityId 机会ID
     * @param status 新状态
     * @return 更新结果
     */
    boolean updateSalesOpportunityStatus(Long opportunityId, String status);

    /**
     * 创建合同
     * 
     * @param contract 合同信息
     * @return 创建结果
     */
    boolean createContract(Contract contract);

    /**
     * 合同审批流程
     * 
     * @param contractId 合同ID
     * @param approverId 审批人ID
     * @param approved 是否通过
     * @param comments 审批意见
     * @return 审批结果
     */
    boolean approveContract(Long contractId, Long approverId, boolean approved, String comments);

    /**
     * 创建项目
     * 
     * @param project 项目信息
     * @return 创建结果
     */
    boolean createProject(Project project);

    /**
     * 更新项目进度
     * 
     * @param projectId 项目ID
     * @param progress 进度百分比
     * @return 更新结果
     */
    boolean updateProjectProgress(Long projectId, BigDecimal progress);

    /**
     * 生成发票
     * 
     * @param projectId 项目ID
     * @param amount 发票金额
     * @return 发票信息
     */
    Invoice generateInvoice(Long projectId, BigDecimal amount);

    /**
     * 确认收款
     * 
     * @param invoiceId 发票ID
     * @param paymentAmount 收款金额
     * @param paymentMethod 收款方式
     * @return 收款确认结果
     */
    boolean confirmPayment(Long invoiceId, BigDecimal paymentAmount, String paymentMethod);

    /**
     * 执行完整的销售到收款流程
     * 
     * @param customerId 客户ID
     * @param opportunityAmount 机会金额
     * @param contractAmount 合同金额
     * @return 流程执行结果
     */
    Map<String, Object> executeSalesToReceiptWorkflow(Long customerId, BigDecimal opportunityAmount, BigDecimal contractAmount);

    // ==================== 采购到付款流程 ====================

    /**
     * 创建采购申请
     * 
     * @param request 采购申请信息
     * @return 创建结果
     */
    boolean createPurchaseRequest(PurchaseRequest request);

    /**
     * 采购申请审批
     * 
     * @param requestId 申请ID
     * @param approverId 审批人ID
     * @param approved 是否通过
     * @param comments 审批意见
     * @return 审批结果
     */
    boolean approvePurchaseRequest(Long requestId, Long approverId, boolean approved, String comments);

    /**
     * 选择供应商
     * 
     * @param requestId 采购申请ID
     * @param supplierId 供应商ID
     * @param bidAmount 投标金额
     * @return 选择结果
     */
    boolean selectSupplier(Long requestId, Long supplierId, BigDecimal bidAmount);

    /**
     * 创建收货记录
     * 
     * @param requestId 采购申请ID
     * @param receivedQuantity 收货数量
     * @param qualityStatus 质量状态
     * @return 收货结果
     */
    boolean createReceipt(Long requestId, Integer receivedQuantity, String qualityStatus);

    /**
     * 处理供应商发票
     * 
     * @param receiptId 收货记录ID
     * @param invoiceAmount 发票金额
     * @param invoiceNumber 发票号码
     * @return 处理结果
     */
    boolean processSupplierInvoice(Long receiptId, BigDecimal invoiceAmount, String invoiceNumber);

    /**
     * 付款审批
     * 
     * @param invoiceId 发票ID
     * @param approverId 审批人ID
     * @param approved 是否通过
     * @param comments 审批意见
     * @return 审批结果
     */
    boolean approvePayment(Long invoiceId, Long approverId, boolean approved, String comments);

    /**
     * 执行付款
     * 
     * @param invoiceId 发票ID
     * @param paymentAmount 付款金额
     * @param paymentMethod 付款方式
     * @return 付款结果
     */
    boolean executePayment(Long invoiceId, BigDecimal paymentAmount, String paymentMethod);

    /**
     * 执行完整的采购到付款流程
     * 
     * @param departmentId 部门ID
     * @param itemName 物品名称
     * @param quantity 数量
     * @param estimatedAmount 预估金额
     * @return 流程执行结果
     */
    Map<String, Object> executePurchaseToPaymentWorkflow(Long departmentId, String itemName, Integer quantity, BigDecimal estimatedAmount);

    // ==================== 费用报销流程 ====================

    /**
     * 创建费用报销申请
     * 
     * @param request 费用报销申请信息
     * @return 创建结果
     */
    boolean createExpenseRequest(ExpenseRequest request);

    /**
     * 上传报销单据
     * 
     * @param requestId 报销申请ID
     * @param attachmentType 附件类型
     * @param attachmentUrl 附件URL
     * @return 上传结果
     */
    boolean uploadExpenseAttachment(Long requestId, String attachmentType, String attachmentUrl);

    /**
     * 部门经理审批
     * 
     * @param requestId 报销申请ID
     * @param managerId 经理ID
     * @param approved 是否通过
     * @param comments 审批意见
     * @return 审批结果
     */
    boolean managerApprove(Long requestId, Long managerId, boolean approved, String comments);

    /**
     * 财务审核
     * 
     * @param requestId 报销申请ID
     * @param financeId 财务人员ID
     * @param approved 是否通过
     * @param comments 审核意见
     * @return 审核结果
     */
    boolean financeReview(Long requestId, Long financeId, boolean approved, String comments);

    /**
     * 总经理审批（大额费用）
     * 
     * @param requestId 报销申请ID
     * @param ceoId CEO ID
     * @param approved 是否通过
     * @param comments 审批意见
     * @return 审批结果
     */
    boolean ceoApprove(Long requestId, Long ceoId, boolean approved, String comments);

    /**
     * 生成会计凭证
     * 
     * @param requestId 报销申请ID
     * @return 凭证生成结果
     */
    boolean generateAccountingVoucher(Long requestId);

    /**
     * 执行付款
     * 
     * @param requestId 报销申请ID
     * @param paymentAmount 付款金额
     * @param paymentMethod 付款方式
     * @return 付款结果
     */
    boolean executeExpensePayment(Long requestId, BigDecimal paymentAmount, String paymentMethod);

    /**
     * 执行完整的费用报销流程
     * 
     * @param employeeId 员工ID
     * @param expenseType 费用类型
     * @param amount 金额
     * @param description 描述
     * @return 流程执行结果
     */
    Map<String, Object> executeExpenseReimbursementWorkflow(Long employeeId, String expenseType, BigDecimal amount, String description);

    // ==================== 流程查询 ====================

    /**
     * 获取销售机会列表
     * 
     * @param customerId 客户ID
     * @param status 状态
     * @return 销售机会列表
     */
    List<SalesOpportunity> getSalesOpportunities(Long customerId, String status);

    /**
     * 获取合同列表
     * 
     * @param customerId 客户ID
     * @param status 状态
     * @return 合同列表
     */
    List<Contract> getContracts(Long customerId, String status);

    /**
     * 获取项目列表
     * 
     * @param contractId 合同ID
     * @param status 状态
     * @return 项目列表
     */
    List<Project> getProjects(Long contractId, String status);

    /**
     * 获取发票列表
     * 
     * @param projectId 项目ID
     * @param status 状态
     * @return 发票列表
     */
    List<Invoice> getInvoices(Long projectId, String status);

    /**
     * 获取采购申请列表
     * 
     * @param departmentId 部门ID
     * @param status 状态
     * @return 采购申请列表
     */
    List<PurchaseRequest> getPurchaseRequests(Long departmentId, String status);

    /**
     * 获取费用报销申请列表
     * 
     * @param employeeId 员工ID
     * @param status 状态
     * @return 费用报销申请列表
     */
    List<ExpenseRequest> getExpenseRequests(Long employeeId, String status);

    /**
     * 获取审批记录
     * 
     * @param businessType 业务类型
     * @param businessId 业务ID
     * @return 审批记录列表
     */
    List<Approval> getApprovals(String businessType, Long businessId);

    /**
     * 获取流程统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getWorkflowStatistics();
} 