<script setup lang="ts">
import { ref, Ref, onBeforeMount, computed } from "vue";
import "plus-pro-components/es/components/form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusForm
} from "plus-pro-components";

import {
  UpdateQbittorrentDownloaderSettingsReq,
  QbittorrentDownloaderSettingsResp,
  getQbittorrentDownloaderSettings,
  updateQbittorrentDownloaderSettings
} from "@/api/systemSettings";
import { message } from "@/utils/message";

defineOptions({
  // name 作为一种规范最好必须写上并且和路由的name保持一致
  name: "QbittorrentDownloaderSettingsForm"
});

// 配置的表单状态
const settingsState = ref<FieldValues>({
  baseUrl: "", // 基础url
  username: "", // qb的用户名
  password: "", // qb的密码
  downloadDir: "" // qb下载器内的路径
});

// 初始化设置的表单
async function initSettingsForm() {
  const resp: QbittorrentDownloaderSettingsResp =
    await getQbittorrentDownloaderSettings();
  if (!resp.success) {
    return;
  }
  setSettingsState(resp);
}

function setSettingsState(resp: QbittorrentDownloaderSettingsResp) {
  settingsState.value.baseUrl = resp.data.baseUrl ?? "";
  settingsState.value.username = resp.data.username ?? "";
  settingsState.value.password = resp.data.password ?? "";
  settingsState.value.downloadDir = resp.data.downloadDir ?? "";
}

onBeforeMount(() => {
  initSettingsForm();
});

// 相关规则
const settingsRules = {
  baseUrl: [
    {
      required: true,
      message: "请输入基础url"
    }
  ],
  username: [
    {
      required: true,
      message: "请输入用户名"
    }
  ],
  password: [
    {
      required: true,
      message: "请输入密码"
    }
  ],
  downloadDir: [
    {
      required: true,
      message: "请输入下载目录"
    }
  ]
};

const settingsColumns: PlusColumn[] = [
  {
    label: "基础url",
    width: 100,
    prop: "baseUrl",
    valueType: "copy",
    colProps: {
      span: 24
    },
    renderExtra: () => `注意: 不需要用 / 结尾`
  },
  {
    label: "用户名",
    width: 100,
    prop: "username",
    valueType: "copy",
    colProps: {
      span: 24
    }
  },
  {
    label: "密码",
    width: 100,
    prop: "password",
    valueType: "copy",
    fieldProps: { type: "password", showPassword: true },
    colProps: {
      span: 24
    }
  },
  {
    label: "下载目录",
    width: 100,
    prop: "downloadDir",
    valueType: "copy",
    colProps: {
      span: 24
    },
    renderExtra: () => `注意:  如果qb在容器中，那么下载目录是qb容器的中绝对路径`
  }
];

const handleSubmit = async (values: FieldValues) => {
  const req: UpdateQbittorrentDownloaderSettingsReq = {
    baseUrl: (settingsState.value.baseUrl as string).trim(),
    username: (settingsState.value.username as string).trim(),
    password: (settingsState.value.password as string).trim(),
    downloadDir: (settingsState.value.downloadDir as string).trim()
  };

  const resp: QbittorrentDownloaderSettingsResp =
    await updateQbittorrentDownloaderSettings(req);

  if (!resp.success) {
    return;
  }
  message("保存成功", { type: "success" });
  setSettingsState(resp);
};
</script>

<template>
  <PlusForm
    label-position="top"
    v-model="settingsState"
    :rules="settingsRules"
    :columns="settingsColumns"
    :row-props="{ gutter: 20 }"
    footerAlign="right"
    @submit="handleSubmit"
  >
  </PlusForm>
</template>
