<script setup lang="ts">
import { ref, Ref, onBeforeMount, h } from "vue";
import "plus-pro-components/es/components/form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusForm
} from "plus-pro-components";

import {
  UpdateEpisodeRenameTaskSettingsReq,
  EpisodeRenameTaskSettingsResp,
  getEpisodeRenameTaskSettings,
  updateEpisodeRenameTaskSettings
} from "@/api/systemSettings";
import { message } from "@/utils/message";

import { ElButton } from "element-plus";

import RemoteFileSelectDialogForm from "@/components/RemoteFileSelectDialogForm/index.vue";

defineOptions({
  // name 作为一种规范最好必须写上并且和路由的name保持一致
  name: "EpisodeRenameTaskSettingsFrom"
});

// 配置的表单状态
const settingsState = ref<FieldValues>({
  sourceDirPath: "", // 地址
  outDirPath: "" // 输出目录路径
});

// 初始化设置的表单
async function initSettingsForm() {
  const resp: EpisodeRenameTaskSettingsResp =
    await getEpisodeRenameTaskSettings();
  if (!resp.success) {
    return;
  }
  setSettingsState(resp);
}

function setSettingsState(resp: EpisodeRenameTaskSettingsResp) {
  settingsState.value.sourceDirPath = resp.data.sourceDirPath ?? "";
  settingsState.value.outDirPath = resp.data.outDirPath ?? "";
}

onBeforeMount(() => {
  initSettingsForm();
});

// 相关规则
const settingsRules = {
  sourceDirPath: [
    {
      required: true,
      message: "请输入源目录路径"
    }
  ],
  outDirPath: [
    {
      required: true,
      message: "请输入输出目录路径"
    }
  ]
};

const settingsColumns: PlusColumn[] = [
  {
    label: "源目录路径",
    width: 100,
    prop: "sourceDirPath",
    valueType: "copy",
    colProps: {
      span: 22
    }
  },
  {
    label: "",
    renderLabel: () => {
      return "";
    },
    prop: "selectSourceDirButton",
    renderField: () => {
      return h(
        ElButton,
        {
          onClick: handleClickSelectSourceDirButton
        },
        () => "..."
      );
    },
    colProps: {
      span: 2
    }
  },
  {
    label: "输出目录路径",
    width: 100,
    prop: "outDirPath",
    valueType: "copy",
    colProps: {
      span: 24
    }
  }
];

const handleSubmit = async (values: FieldValues) => {
  const req: UpdateEpisodeRenameTaskSettingsReq = {
    sourceDirPath: (settingsState.value.sourceDirPath as string).trim(),
    outDirPath: (settingsState.value.outDirPath as string).trim()
  };

  const resp: EpisodeRenameTaskSettingsResp =
    await updateEpisodeRenameTaskSettings(req);

  if (!resp.success) {
    return;
  }
  message("保存成功", { type: "success" });
  setSettingsState(resp);
};

const selectSourceDirFormVisible = ref(false);
const handleClickSelectSourceDirButton = () => {
  selectSourceDirFormVisible.value = true;
};

function closeSelectSourceDirForm() {
  selectSourceDirFormVisible.value = false;
}

function onSelectSourceDirSuccess(path: string) {
  settingsState.value.sourceDirPath = path;
}
</script>

<template>
  <PlusForm
    v-model="settingsState"
    label-position="top"
    :rules="settingsRules"
    :columns="settingsColumns"
    :row-props="{ gutter: 20 }"
    footerAlign="right"
    @submit="handleSubmit"
  />

  <remote-file-select-dialog-form
    v-if="selectSourceDirFormVisible"
    root-path="C:\Users\18581\Desktop"
    @closeForm="closeSelectSourceDirForm"
    @select-success="onSelectSourceDirSuccess"
  />
</template>
