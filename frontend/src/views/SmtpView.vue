<script setup lang="ts">
import { $axios } from '@/axios'
import SmtpConfig from '@/components/Smtp/Smtp-config.vue';
import SmtpForm from '@/components/Smtp/Smtp-form.vue'
import type { SmtpData } from '@/types'
import { ref } from 'vue'

</script>
<template>
  <div class="topbar-container">
    <div class="column">
      <div class="title flex items-center q-pl-md">
        <div><strong>SMTP</strong></div>
      </div>
    </div>
    <SmtpForm></SmtpForm>
  </div>
</template>
