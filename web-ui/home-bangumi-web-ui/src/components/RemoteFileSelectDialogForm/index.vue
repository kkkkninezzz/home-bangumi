<script setup lang="ts">
import { ref, onBeforeMount, computed } from "vue";
import "plus-pro-components/es/components/form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusDialogForm
} from "plus-pro-components";

import {
  ChildFileInfo,
  PreViewFilesResp,
  preViewFiles
} from "@/api/filePreView";

import type Node from "element-plus/es/components/tree/src/model/node";

import { message } from "@/utils/message";
import { array, string } from "vue-types";

defineOptions({
  name: "RemoteFileSelectDialogForm"
});

const visible = ref(true);
const previewLoading = ref(false);

// 关闭表单时通知父组件
const emit = defineEmits<{
  (e: "close-form"): void;
  (e: "select-success", path: string): void;
}>();

// 表单状态
const formState = ref<FieldValues>({
  path: null // 路径
});

/** 初始化表单 */
function initFormState() {}

onBeforeMount(() => {
  initFormState();
});

// 节点属性
const fileNodeProps = {
  label: "name",
  children: "children",
  isLeaf: "isDir"
};

// 获取父节点的名字列表
const getParentNodeFileNames = (node: Node): Array<string> => {
  if (node.parent == null) {
    return [node.label];
  }

  const result = [];
  let n = node;
  while (n != null) {
    result.push(n.label);
    n = n.parent;
  }
  return result.reverse();
};

const loadFileChildren = (
  node: Node,
  resolve: (data: ChildFileInfo[]) => void
) => {
  if (node.level === 0) {
    return resolve([{ path: "region", name: "", isFile: false }]);
  }
  // TODO 进行请求
};

// 表单相关规则
const formRules = {
  path: [
    {
      required: true,
      message: "请选择文件路径"
    }
  ]
};

// rss链接表单的列
const rssLinkColumns: PlusColumn[] = [
  {
    label: "文件目录",
    prop: "path",
    valueType: "tree-select",
    fieldProps: {
      props: fileNodeProps,
      accordion: true,
      lazy: true,
      load: loadFileChildren
    }
  }
];

const handleSelect = async (
  handleSubmit: () => Promise<boolean>,
  handleReset: () => void
) => {
  const isPass = await handleSubmit();
  if (!isPass) {
    return;
  }
  handleCloseForm(handleReset);
  // 抛出选择成功事件
  emit("select-success", "resp.data.id");
};

const handleCloseForm = async (handleReset: () => void) => {
  await handleReset();
  visible.value = false;
  emit("close-form");
};
</script>
<template>
  <PlusDialogForm
    v-model:visible="visible"
    v-model="formState"
    :dialog="{ title: '文件列表', hasFooter: false }"
    :form="{
      columns: rssLinkColumns,
      rules: formRules,
      rowProps: { gutter: 20 },
      footerAlign: 'right',
      hasFooter: true,
      labelPosition: 'top'
    }"
  >
    <template #form-footer="{ handleSubmit, handleReset }">
      <el-button type="danger" @click="handleCloseForm(handleReset)">
        返回</el-button
      >
      <el-button
        type="primary"
        :loading="previewLoading"
        @click="handleSelect(handleSubmit, handleReset)"
        >确认</el-button
      >
    </template>
  </PlusDialogForm>
</template>
