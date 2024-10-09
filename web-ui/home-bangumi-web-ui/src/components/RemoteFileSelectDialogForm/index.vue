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

defineOptions({
  name: "RemoteFileSelectDialogForm"
});

const props = withDefaults(
  defineProps<{
    rootPath: string;
  }>(),
  {}
);

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
function initFormState() {
  formState.value.path = null;
}

onBeforeMount(() => {
  initFormState();
});

// 节点属性
const fileNodeProps = {
  label: "name",
  children: "children",
  isLeaf: "isFile",
  value: "path",
  disabled: "disabled"
};

type FileInfoProps = {
  /**
   * 绝对路径
   */
  path: string;

  /**
   * 当前的文件名或者目录名
   *
   * */
  name: string;

  /**
   * 是否为文件
   * */
  isFile: boolean;

  /**
   * 是否禁用
   */
  disabled: boolean;
};

const loadFileChildren = async (
  node: Node,
  resolve: (data: FileInfoProps[]) => void
) => {
  if (node.level === 0) {
    return resolve([
      {
        path: props.rootPath,
        name: props.rootPath,
        isFile: false,
        disabled: false
      }
    ]);
  }

  const path = node.data.path;
  const resp: PreViewFilesResp = await preViewFiles(path);
  if (!resp.success) {
    return resolve([]);
  }

  // 如果当前路径是文件
  if (resp.data.isFile) {
    return resolve([]);
  }

  const children: Array<FileInfoProps> = resp.data.children.map(child => {
    return {
      path: child.path,
      name: child.name,
      isFile: child.isFile,
      disabled: child.isFile
    };
  });

  resolve(children);
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
      load: loadFileChildren,
      checkStrictly: true
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
  emit("select-success", formState.value.path as string);
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
