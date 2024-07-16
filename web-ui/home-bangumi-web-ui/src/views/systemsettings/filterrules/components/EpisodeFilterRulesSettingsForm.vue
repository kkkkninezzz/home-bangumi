<script setup lang="ts">
import { ref, Ref, onBeforeMount, computed } from "vue";
import "plus-pro-components/es/components/form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusForm
} from "plus-pro-components";

import {
  EpisodeFilterRulesSettingsResp,
  EpisodeFilterRulesSettingsReq,
  getEpisodeFilterRulesSettings,
  updateEpisodeFilterRulesSettings
} from "@/api/systemSettings";
import { message } from "@/utils/message";

defineOptions({
  // name 作为一种规范最好必须写上并且和路由的name保持一致
  name: "EpisodeFilterRulesSettingsForm"
});

// 配置的表单状态
const settingsState = ref<FieldValues>({
  rules: [] // 规则
});

// 初始化设置的表单
async function initSettingsForm() {
  const resp: EpisodeFilterRulesSettingsResp =
    await getEpisodeFilterRulesSettings();
  if (!resp.success) {
    return;
  }
  setSettingsState(resp);
}

function setSettingsState(resp: EpisodeFilterRulesSettingsResp) {
  settingsState.value.rules = resp.data.rules ?? [];
}

onBeforeMount(() => {
  initSettingsForm();
});

// 相关规则
const settingsRules = {};

const settingsColumns: PlusColumn[] = [
  {
    label: "rss剧集默认过滤规则",
    width: 120,
    prop: "rules",
    renderExtra: () =>
      `如果rss链接解析出来的原始标题包含对应规则，那么会过滤掉对应的数据`,
    valueType: "plus-input-tag",
    colProps: {
      span: 24
    }
  }
];

const handleSubmit = async (values: FieldValues) => {
  const req: EpisodeFilterRulesSettingsReq = {
    rules: settingsState.value.rules as Array<string>
  };

  const resp: EpisodeFilterRulesSettingsResp =
    await updateEpisodeFilterRulesSettings(req);

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
