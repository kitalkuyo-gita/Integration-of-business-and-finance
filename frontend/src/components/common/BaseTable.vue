<template>
  <div class="base-table">
    <!-- 表格工具栏 -->
    <div class="table-toolbar" v-if="showToolbar">
      <div class="toolbar-left">
        <slot name="toolbar-left">
          <el-button 
            v-if="showAdd" 
            type="primary" 
            icon="el-icon-plus" 
            @click="handleAdd"
          >
            {{ addText }}
          </el-button>
          <el-button 
            v-if="showBatchDelete" 
            type="danger" 
            icon="el-icon-delete" 
            :disabled="!hasSelection"
            @click="handleBatchDelete"
          >
            {{ batchDeleteText }}
          </el-button>
        </slot>
      </div>
      <div class="toolbar-right">
        <slot name="toolbar-right">
          <el-button 
            icon="el-icon-refresh" 
            @click="handleRefresh"
          >
            刷新
          </el-button>
        </slot>
      </div>
    </div>

    <!-- 表格 -->
    <el-table
      ref="table"
      v-loading="loading"
      :data="tableData"
      :height="height"
      :max-height="maxHeight"
      :stripe="stripe"
      :border="border"
      :size="size"
      :fit="fit"
      :show-header="showHeader"
      :highlight-current-row="highlightCurrentRow"
      :current-row-key="currentRowKey"
      :row-class-name="rowClassName"
      :row-style="rowStyle"
      :cell-class-name="cellClassName"
      :cell-style="cellStyle"
      :header-row-class-name="headerRowClassName"
      :header-row-style="headerRowStyle"
      :header-cell-class-name="headerCellClassName"
      :header-cell-style="headerCellStyle"
      :row-key="rowKey"
      :empty-text="emptyText"
      :default-expand-all="defaultExpandAll"
      :expand-row-keys="expandRowKeys"
      :default-sort="defaultSort"
      :tooltip-effect="tooltipEffect"
      :show-summary="showSummary"
      :sum-text="sumText"
      :summary-method="summaryMethod"
      :span-method="spanMethod"
      :select-on-indeterminate="selectOnIndeterminate"
      :indent="indent"
      :lazy="lazy"
      :load="load"
      :tree-props="treeProps"
      :table-layout="tableLayout"
      :scrollbar-always-on="scrollbarAlwaysOn"
      :flexible="flexible"
      @select="handleSelect"
      @select-all="handleSelectAll"
      @selection-change="handleSelectionChange"
      @cell-mouse-enter="handleCellMouseEnter"
      @cell-mouse-leave="handleCellMouseLeave"
      @cell-click="handleCellClick"
      @cell-dblclick="handleCellDblclick"
      @row-click="handleRowClick"
      @row-contextmenu="handleRowContextmenu"
      @row-dblclick="handleRowDblclick"
      @header-click="handleHeaderClick"
      @header-contextmenu="handleHeaderContextmenu"
      @sort-change="handleSortChange"
      @filter-change="handleFilterChange"
      @header-dragend="handleHeaderDragend"
      @expand-change="handleExpandChange"
    >
      <!-- 选择列 -->
      <el-table-column
        v-if="showSelection"
        type="selection"
        width="55"
        align="center"
        fixed="left"
      />

      <!-- 序号列 -->
      <el-table-column
        v-if="showIndex"
        type="index"
        label="序号"
        width="60"
        align="center"
        fixed="left"
      />

      <!-- 动态列 -->
      <slot />

      <!-- 操作列 -->
      <el-table-column
        v-if="showAction"
        label="操作"
        :width="actionWidth"
        align="center"
        fixed="right"
      >
        <template slot-scope="scope">
          <slot name="action" :row="scope.row" :$index="scope.$index">
            <el-button
              v-if="showEdit"
              type="text"
              size="small"
              @click="handleEdit(scope.row, scope.$index)"
            >
              编辑
            </el-button>
            <el-button
              v-if="showDelete"
              type="text"
              size="small"
              style="color: #F56C6C"
              @click="handleDelete(scope.row, scope.$index)"
            >
              删除
            </el-button>
          </slot>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="table-pagination" v-if="showPagination">
      <el-pagination
        :current-page="pagination.current"
        :page-sizes="pagination.pageSizes"
        :page-size="pagination.size"
        :layout="pagination.layout"
        :total="pagination.total"
        :small="pagination.small"
        :background="pagination.background"
        :pager-count="pagination.pagerCount"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: 'BaseTable',
  props: {
    // 表格数据
    data: {
      type: Array,
      default: () => []
    },
    // 加载状态
    loading: {
      type: Boolean,
      default: false
    },
    // 表格高度
    height: {
      type: [String, Number],
      default: null
    },
    // 表格最大高度
    maxHeight: {
      type: [String, Number],
      default: null
    },
    // 是否为斑马纹表格
    stripe: {
      type: Boolean,
      default: true
    },
    // 是否带有纵向边框
    border: {
      type: Boolean,
      default: true
    },
    // 表格的尺寸
    size: {
      type: String,
      default: 'medium'
    },
    // 列的宽度是否自撑开
    fit: {
      type: Boolean,
      default: true
    },
    // 是否显示表头
    showHeader: {
      type: Boolean,
      default: true
    },
    // 是否要高亮当前行
    highlightCurrentRow: {
      type: Boolean,
      default: false
    },
    // 当前行的 key，只写 String
    currentRowKey: {
      type: [String, Number],
      default: null
    },
    // 行的 className 的回调方法
    rowClassName: {
      type: [String, Function],
      default: null
    },
    // 行的 style 的回调方法
    rowStyle: {
      type: [Object, Function],
      default: null
    },
    // 单元格的 className 的回调方法
    cellClassName: {
      type: [String, Function],
      default: null
    },
    // 单元格的 style 的回调方法
    cellStyle: {
      type: [Object, Function],
      default: null
    },
    // 表头行的 className 的回调方法
    headerRowClassName: {
      type: [String, Function],
      default: null
    },
    // 表头行的 style 的回调方法
    headerRowStyle: {
      type: [Object, Function],
      default: null
    },
    // 表头单元格的 className 的回调方法
    headerCellClassName: {
      type: [String, Function],
      default: null
    },
    // 表头单元格的 style 的回调方法
    headerCellStyle: {
      type: [Object, Function],
      default: null
    },
    // 行数据的 Key，用来优化 Table 的渲染
    rowKey: {
      type: [String, Function],
      default: 'id'
    },
    // 空数据时显示的文本内容
    emptyText: {
      type: String,
      default: '暂无数据'
    },
    // 是否默认展开所有行
    defaultExpandAll: {
      type: Boolean,
      default: false
    },
    // 展开行的 keys 数组
    expandRowKeys: {
      type: Array,
      default: () => []
    },
    // 默认的排序列的 prop 和顺序
    defaultSort: {
      type: Object,
      default: null
    },
    // tooltip effect 属性
    tooltipEffect: {
      type: String,
      default: 'dark'
    },
    // 是否在表尾显示合计行
    showSummary: {
      type: Boolean,
      default: false
    },
    // 合计行第一列的文本
    sumText: {
      type: String,
      default: '合计'
    },
    // 自定义的合计计算方法
    summaryMethod: {
      type: Function,
      default: null
    },
    // 合并行或列的计算方法
    spanMethod: {
      type: Function,
      default: null
    },
    // 在多选表格中，当只有部分行被选中时，点击表头的 checkbox 时的行为
    selectOnIndeterminate: {
      type: Boolean,
      default: true
    },
    // 展示树形数据时，树节点的缩进
    indent: {
      type: Number,
      default: 16
    },
    // 是否懒加载子节点数据
    lazy: {
      type: Boolean,
      default: false
    },
    // 加载子节点数据的方法
    load: {
      type: Function,
      default: null
    },
    // 渲染嵌套数据的配置选项
    treeProps: {
      type: Object,
      default: () => ({
        hasChildren: 'hasChildren',
        children: 'children'
      })
    },
    // 设置表格单元、行和列的布局方式
    tableLayout: {
      type: String,
      default: 'fixed'
    },
    // 总是显示滚动条
    scrollbarAlwaysOn: {
      type: Boolean,
      default: false
    },
    // 是否为 flex 布局
    flexible: {
      type: Boolean,
      default: false
    },
    // 是否显示工具栏
    showToolbar: {
      type: Boolean,
      default: true
    },
    // 是否显示添加按钮
    showAdd: {
      type: Boolean,
      default: true
    },
    // 添加按钮文本
    addText: {
      type: String,
      default: '新增'
    },
    // 是否显示批量删除按钮
    showBatchDelete: {
      type: Boolean,
      default: true
    },
    // 批量删除按钮文本
    batchDeleteText: {
      type: String,
      default: '批量删除'
    },
    // 是否显示选择列
    showSelection: {
      type: Boolean,
      default: false
    },
    // 是否显示序号列
    showIndex: {
      type: Boolean,
      default: true
    },
    // 是否显示操作列
    showAction: {
      type: Boolean,
      default: true
    },
    // 操作列宽度
    actionWidth: {
      type: [String, Number],
      default: 150
    },
    // 是否显示编辑按钮
    showEdit: {
      type: Boolean,
      default: true
    },
    // 是否显示删除按钮
    showDelete: {
      type: Boolean,
      default: true
    },
    // 是否显示分页
    showPagination: {
      type: Boolean,
      default: true
    },
    // 分页配置
    pagination: {
      type: Object,
      default: () => ({
        current: 1,
        size: 10,
        total: 0,
        pageSizes: [10, 20, 50, 100],
        layout: 'total, sizes, prev, pager, next, jumper',
        small: false,
        background: true,
        pagerCount: 7
      })
    }
  },
  data() {
    return {
      // 选中的行
      selection: []
    }
  },
  computed: {
    // 表格数据
    tableData() {
      return this.data
    },
    // 是否有选中的行
    hasSelection() {
      return this.selection.length > 0
    }
  },
  methods: {
    // 处理添加
    handleAdd() {
      this.$emit('add')
    },
    // 处理批量删除
    handleBatchDelete() {
      this.$emit('batch-delete', this.selection)
    },
    // 处理刷新
    handleRefresh() {
      this.$emit('refresh')
    },
    // 处理编辑
    handleEdit(row, index) {
      this.$emit('edit', row, index)
    },
    // 处理删除
    handleDelete(row, index) {
      this.$emit('delete', row, index)
    },
    // 处理选择
    handleSelect(selection, row) {
      this.$emit('select', selection, row)
    },
    // 处理全选
    handleSelectAll(selection) {
      this.$emit('select-all', selection)
    },
    // 处理选择变化
    handleSelectionChange(selection) {
      this.selection = selection
      this.$emit('selection-change', selection)
    },
    // 处理单元格鼠标进入
    handleCellMouseEnter(row, column, cell, event) {
      this.$emit('cell-mouse-enter', row, column, cell, event)
    },
    // 处理单元格鼠标离开
    handleCellMouseLeave(row, column, cell, event) {
      this.$emit('cell-mouse-leave', row, column, cell, event)
    },
    // 处理单元格点击
    handleCellClick(row, column, cell, event) {
      this.$emit('cell-click', row, column, cell, event)
    },
    // 处理单元格双击
    handleCellDblclick(row, column, cell, event) {
      this.$emit('cell-dblclick', row, column, cell, event)
    },
    // 处理行点击
    handleRowClick(row, column, event) {
      this.$emit('row-click', row, column, event)
    },
    // 处理行右键
    handleRowContextmenu(row, column, event) {
      this.$emit('row-contextmenu', row, column, event)
    },
    // 处理行双击
    handleRowDblclick(row, column, event) {
      this.$emit('row-dblclick', row, column, event)
    },
    // 处理表头点击
    handleHeaderClick(column, event) {
      this.$emit('header-click', column, event)
    },
    // 处理表头右键
    handleHeaderContextmenu(column, event) {
      this.$emit('header-contextmenu', column, event)
    },
    // 处理排序变化
    handleSortChange({ column, prop, order }) {
      this.$emit('sort-change', { column, prop, order })
    },
    // 处理筛选变化
    handleFilterChange(filters) {
      this.$emit('filter-change', filters)
    },
    // 处理表头拖拽结束
    handleHeaderDragend(newWidth, oldWidth, column, event) {
      this.$emit('header-dragend', newWidth, oldWidth, column, event)
    },
    // 处理展开变化
    handleExpandChange(row, expandedRows) {
      this.$emit('expand-change', row, expandedRows)
    },
    // 处理分页大小变化
    handleSizeChange(size) {
      this.$emit('size-change', size)
    },
    // 处理当前页变化
    handleCurrentChange(current) {
      this.$emit('current-change', current)
    },
    // 清空选择
    clearSelection() {
      this.$refs.table.clearSelection()
    },
    // 切换行选择状态
    toggleRowSelection(row, selected) {
      this.$refs.table.toggleRowSelection(row, selected)
    },
    // 切换全选状态
    toggleAllSelection() {
      this.$refs.table.toggleAllSelection()
    },
    // 设置当前行
    setCurrentRow(row) {
      this.$refs.table.setCurrentRow(row)
    },
    // 清除排序
    clearSort() {
      this.$refs.table.clearSort()
    },
    // 清除筛选
    clearFilter() {
      this.$refs.table.clearFilter()
    },
    // 手动排序
    sort(prop, order) {
      this.$refs.table.sort(prop, order)
    }
  }
}
</script>

<style lang="scss" scoped>
.base-table {
  .table-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    padding: 8px 0;
    
    .toolbar-left {
      display: flex;
      align-items: center;
      gap: 8px;
    }
    
    .toolbar-right {
      display: flex;
      align-items: center;
      gap: 8px;
    }
  }
  
  .table-pagination {
    display: flex;
    justify-content: flex-end;
    margin-top: 16px;
    padding: 8px 0;
  }
}
</style> 