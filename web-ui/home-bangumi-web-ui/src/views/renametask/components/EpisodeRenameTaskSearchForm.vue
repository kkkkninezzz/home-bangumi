<script setup lang="ts">
import { ref, onBeforeMount } from "vue";
import "plus-pro-components/es/components/form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusSearch
} from "plus-pro-components";

import { EpisodeRenameTaskStatusOptions } from "../enums";

defineOptions({
  name: "EpisodeRenameTaskSearchForm"
});

const props = withDefaults(
  defineProps<{
    loading: boolean;
  }>(),
  {}
);

// 关闭表单时通知父组件
const emit = defineEmits<{
  (e: "search", taskName: string, taskStatus: number): void;
}>();

const state = ref<FieldValues>({
  taskName: "", // 任务名
  taskStatus: null
});

const columns: PlusColumn[] = [
  {
    label: "任务名",
    prop: "taskName",
    valueType: "copy"
  },

  {
    label: "任务状态",
    prop: "taskStatus",
    valueType: "select",
    options: EpisodeRenameTaskStatusOptions
  }
];
const handleSearch = (values: any) => {
  emit(
    "search",
    state.value.taskName as string,
    state.value.taskStatus as number
  );
};
</script>
<template>
  <PlusSearch
    v-model="state"
    :columns="columns"
    :show-number="2"
    :has-label="false"
    :colProps="{ xs: 4, sm: 4, md: 4, lg: 4, xl: 4 }"
    :searchLoading="props.loading"
    @search="handleSearch"
  />
</template>
