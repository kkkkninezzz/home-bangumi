<script setup lang="ts">
import ScheduledTaskSettingsForm from "./components/ScheduledTaskSettingsForm.vue";
defineOptions({
  // name 作为一种规范最好必须写上并且和路由的name保持一致
  name: "ScheduledtaskSettings"
});
</script>

<template>
  <div style="display: flex">
    <div style="flex: 1"></div>

    <el-card style="width: 720px">
      <scheduled-task-settings-form />
    </el-card>
    <div style="flex: 1"></div>
  </div>
</template>
