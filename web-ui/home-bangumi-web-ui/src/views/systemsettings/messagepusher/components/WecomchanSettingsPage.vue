<script setup lang="ts">
import WecomchanSettingsForm from "./WecomchanSettingsForm.vue";
defineOptions({
  // name 作为一种规范最好必须写上并且和路由的name保持一致
  name: "WecomchanSettingsPage"
});
</script>

<template>
  <div style="display: flex">
    <div style="flex: 1" />

    <el-card style="width: 720px">
      <wecomchan-settings-form />
    </el-card>
    <div style="flex: 1" />
  </div>
</template>
