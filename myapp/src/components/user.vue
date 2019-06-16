<template>
    <div>
        <x-header :left-options="{showBack: false}">用户</x-header>
        <group>
            <cell title="我的账户" value="universal42"></cell>
            <cell title="退出" is-link @click.native="login"></cell>
        </group>
    </div>
</template>
<script>
import { XHeader, Cell, Group } from "vux";
export default {
    components: {
        XHeader,
        Cell,
        Group
    },
    methods: {
        login() {
            this.$router.replace({ path: '/' });
        }
    }
};
</script>
<style scoped>
.avator {
    background-color: aqua;
}
.contain {
    background-color: cadetblue;
}
</style>
