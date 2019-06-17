<template>
    <div>
        <div>
            <div>
                <router-view></router-view> 
            </div>
            <tabbar class="tab">
                <tabbar-item selected link="/main">
                    <span slot="label">成绩分析</span>
                </tabbar-item>
                <tabbar-item link="/main/user">
                    <span slot="label">我的账号</span>
                </tabbar-item>
            </tabbar>
        </div>
    </div>
</template>
<script>
import { XHeader, Tabbar, TabbarItem, Group, Cell } from "vux";

export default {
    components: {
        Tabbar,
        TabbarItem,
        Group,
        Cell,
        XHeader
    }
};
</script>
<style scoped>
.tab{
    position: fixed;
}
</style>
