<script setup lang="ts">
import { ref, onBeforeMount } from "vue";
import { ElCard } from "element-plus";
import "plus-pro-components/es/components/form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusSearch
} from "plus-pro-components";

import {
  RssBangumiPreviewReq,
  RssBangumiPreviewResp,
  previewRssBangumi
} from "@/api/rssBangumi";

import { message } from "@/utils/message";

import {
  RssBangumRssCategoryOptions,
  RssBangumiHandleMethodOptions,
  RssBangumiStatusOptions
} from "../enums";

defineOptions({
  name: "RssBangumiSearchForm"
});

const props = withDefaults(
  defineProps<{
    loading: boolean;
  }>(),
  {}
);

// 关闭表单时通知父组件
const emit = defineEmits<{
  (
    e: "search",
    rssNameOrBanugmiTitle: string,
    rssCategory: number,
    handleMethod: number,
    status: number
  ): void;
}>();

const state = ref<FieldValues>({
  rssNameOrBanugmiTitle: "", // 链接名或者番剧标题
  rssCategory: null,
  handleMethod: null,
  status: null
});

const columns: PlusColumn[] = [
  {
    label: "rss链接名或者番剧标题",
    prop: "rssNameOrBanugmiTitle",
    valueType: "copy"
  },
  {
    label: "类别",
    prop: "rssCategory",
    valueType: "select",
    options: RssBangumRssCategoryOptions
  },
  {
    label: "方式",
    prop: "handleMethod",
    valueType: "select",
    options: RssBangumiHandleMethodOptions
  },
  {
    label: "番剧状态",
    prop: "status",
    valueType: "select",
    options: RssBangumiStatusOptions
  }
];
const handleSearch = (values: any) => {
  emit(
    "search",
    state.value.rssNameOrBanugmiTitle as string,
    state.value.rssCategory as number,
    state.value.handleMethod as number,
    state.value.status as number
  );
};
</script>
<template>
  <PlusSearch
    v-model="state"
    :columns="columns"
    :show-number="4"
    :has-label="false"
    :colProps="{ xs: 4, sm: 4, md: 4, lg: 4, xl: 4 }"
    :searchLoading="props.loading"
    @search="handleSearch"
  />
</template>
