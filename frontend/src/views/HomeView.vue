<script setup lang="ts">
import MMERecent from '../components/MME-Recent.vue'
import MSERecent from '../components/MSE-Recent.vue'
</script>

<template>
  <div class="scrollable-content">
    <link href="https://fonts.googleapis.com/css?family=Lato" rel="stylesheet" />

    <div class="content">
      <div class="content__container">
        <p class="content__container__text">Hello</p>

        <ul class="content__container__list">
          <li class="content__container__list__item">world !</li>
          <li class="content__container__list__item">Modbus !</li>
          <li class="content__container__list__item">OPC-UA !</li>
          <li class="content__container__list__item">users !</li>
        </ul>
      </div>
    </div>

    <div class="col column">
      <div class="recent_text">
        <div><strong>Recent </strong></div>
        <div class="info"><q-btn class="info_btn" round color="secondary"></q-btn> Master</div>
        <div class="info"><q-btn class="info_btn" round color="primary"></q-btn> Slave</div>
      </div>
      <div class="q-pa-sm row items-start q-gutter-sm">
        <div class="q-pa-sm colums items-start q-gutter-sm">
          <q-badge outline color="orange" size="md" label="Modbus Master Ethernet" />
          <MMERecent />
        </div>

        <div class="q-pa-sm colums items-start q-gutter-sm">
          <q-badge outline color="orange" size="md" label="Modbus Slave Ethernet" />
          <MSERecent />
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
.scrollable-content {
  position: absolute;
  height: 100%;
  width: 100%;
  background-color: #34495e;
  overflow-y: auto;
  max-height: 80vh;
}

.recent_text {
  font-size: 25px;
  margin-left: 1%;
  margin-top: 10%;
  color: #ecf0f1;

  @media (max-width: 800px) {
    margin-top: 30%;
  }
}

.recent_text div {
  display: inline-block;
  margin-right: 13px;
}

.info {
  font-size: 15px;
}

.info_btn {
  font-size: 5px;
}

.rating {
  margin-top: -8px;
}

.content {
  @media (max-width: 800px) {
    transform: translate(5%, -20%);
  }

  position: absolute;
  top: 15%;
  left: 15%;
  transform: translate(80%, -20%);
  height: 160px;
  overflow: hidden;

  font-family: 'Lato', sans-serif;
  font-size: 35px;
  line-height: 40px;
  color: #ecf0f1;

  &__container {
    font-weight: 600;
    overflow: hidden;
    height: 40px;
    padding: 0 40px;

    &:before {
      content: '[';
      left: 0;
    }

    &:after {
      content: ']';
      position: absolute;
      right: 0;
    }

    &:after,
    &:before {
      position: absolute;
      top: 0;

      color: #16a085;
      font-size: 42px;
      line-height: 40px;

      -webkit-animation-name: opacity;
      -webkit-animation-duration: 2s;
      -webkit-animation-iteration-count: infinite;
      animation-name: opacity;
      animation-duration: 2s;
      animation-iteration-count: infinite;
    }

    &__text {
      display: inline;
      float: left;
      margin: 0;
    }

    &__list {
      margin-top: 0;
      padding-left: 110px;
      text-align: left;
      list-style: none;

      -webkit-animation-name: change;
      -webkit-animation-duration: 10s;
      -webkit-animation-iteration-count: infinite;
      animation-name: change;
      animation-duration: 10s;
      animation-iteration-count: infinite;

      &__item {
        line-height: 40px;
        margin: 0;
      }
    }
  }
}

@-webkit-keyframes opacity {
  0%,
  100% {
    opacity: 0;
  }

  50% {
    opacity: 1;
  }
}

@-webkit-keyframes change {
  0%,
  12.66%,
  100% {
    transform: translate3d(0, 0, 0);
  }

  16.66%,
  29.32% {
    transform: translate3d(0, -25%, 0);
  }

  33.32%,
  45.98% {
    transform: translate3d(0, -50%, 0);
  }

  49.98%,
  62.64% {
    transform: translate3d(0, -75%, 0);
  }

  66.64%,
  79.3% {
    transform: translate3d(0, -50%, 0);
  }

  83.3%,
  95.96% {
    transform: translate3d(0, -25%, 0);
  }
}

@-o-keyframes opacity {
  0%,
  100% {
    opacity: 0;
  }

  50% {
    opacity: 1;
  }
}

@-o-keyframes change {
  0%,
  12.66%,
  100% {
    transform: translate3d(0, 0, 0);
  }

  16.66%,
  29.32% {
    transform: translate3d(0, -25%, 0);
  }

  33.32%,
  45.98% {
    transform: translate3d(0, -50%, 0);
  }

  49.98%,
  62.64% {
    transform: translate3d(0, -75%, 0);
  }

  66.64%,
  79.3% {
    transform: translate3d(0, -50%, 0);
  }

  83.3%,
  95.96% {
    transform: translate3d(0, -25%, 0);
  }
}

@-moz-keyframes opacity {
  0%,
  100% {
    opacity: 0;
  }

  50% {
    opacity: 1;
  }
}

@-moz-keyframes change {
  0%,
  12.66%,
  100% {
    transform: translate3d(0, 0, 0);
  }

  16.66%,
  29.32% {
    transform: translate3d(0, -25%, 0);
  }

  33.32%,
  45.98% {
    transform: translate3d(0, -50%, 0);
  }

  49.98%,
  62.64% {
    transform: translate3d(0, -75%, 0);
  }

  66.64%,
  79.3% {
    transform: translate3d(0, -50%, 0);
  }

  83.3%,
  95.96% {
    transform: translate3d(0, -25%, 0);
  }
}

@keyframes opacity {
  0%,
  100% {
    opacity: 0;
  }

  50% {
    opacity: 1;
  }
}

@keyframes change {
  0%,
  12.66%,
  100% {
    transform: translate3d(0, 0, 0);
  }

  16.66%,
  29.32% {
    transform: translate3d(0, -25%, 0);
  }

  33.32%,
  45.98% {
    transform: translate3d(0, -50%, 0);
  }

  49.98%,
  62.64% {
    transform: translate3d(0, -75%, 0);
  }

  66.64%,
  79.3% {
    transform: translate3d(0, -50%, 0);
  }

  83.3%,
  95.96% {
    transform: translate3d(0, -25%, 0);
  }
}
</style>
