<script setup lang="ts">
import { ref, Ref, onBeforeMount, computed } from "vue";
import "plus-pro-components/es/components/form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusForm
} from "plus-pro-components";

import {
  UpdateWecomchanSettingsReq,
  WecomchanSettingsResp,
  getWecomchanSettings,
  updateWecomchanSettings
} from "@/api/systemSettings";
import { message } from "@/utils/message";

defineOptions({
  // name 作为一种规范最好必须写上并且和路由的name保持一致
  name: "WecomchanSettingsForm"
});

// 配置的表单状态
const settingsState = ref<FieldValues>({
  enable: false, // 是否可用
  url: "", // 地址
  sendKey: "" // 发送key
});

// 初始化设置的表单
async function initSettingsForm() {
  const resp: WecomchanSettingsResp = await getWecomchanSettings();
  if (!resp.success) {
    return;
  }
  setSettingsState(resp);
}

function setSettingsState(resp: WecomchanSettingsResp) {
  settingsState.value.enable = resp.data.enable ?? false;
  settingsState.value.url = resp.data.url ?? "";
  settingsState.value.sendKey = resp.data.sendKey ?? "";
}

onBeforeMount(() => {
  initSettingsForm();
});

// 相关规则
const settingsRules = {
  enable: [
    {
      required: true,
      message: "请确认是否启用"
    }
  ],
  url: [
    {
      required: true,
      message: "请输入服务器地址"
    }
  ],
  sendKey: [
    {
      required: true,
      message: "请输入sendKey"
    }
  ]
};

const settingsColumns: PlusColumn[] = [
  {
    label: "是否启用",
    width: 100,
    prop: "enable",
    valueType: "switch",
    colProps: {
      span: 24
    }
  },
  {
    label: "服务器地址",
    width: 100,
    prop: "url",
    valueType: "copy",
    colProps: {
      span: 24
    }
  },
  {
    label: "sendKey",
    width: 100,
    prop: "sendKey",
    valueType: "copy",
    fieldProps: { type: "password", showPassword: true },
    colProps: {
      span: 24
    }
  }
];

const handleSubmit = async (values: FieldValues) => {
  const req: UpdateWecomchanSettingsReq = {
    enable: settingsState.value.enable as boolean,
    url: (settingsState.value.url as string).trim(),
    sendKey: (settingsState.value.sendKey as string).trim()
  };

  const resp: WecomchanSettingsResp = await updateWecomchanSettings(req);

  if (!resp.success) {
    return;
  }
  message("保存成功", { type: "success" });
  setSettingsState(resp);
};
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
</template>
