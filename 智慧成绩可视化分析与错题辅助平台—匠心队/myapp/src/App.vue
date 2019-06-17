<template>
    <div id="app">
        <div>
            <router-view></router-view>
        </div>
        <!-- <div v-show="!islogin">
            <div>
                <router-view></router-view>
            </div>
            <tabbar>
                <tabbar-item selected link="/rank">
                    <span slot="label">成绩分析</span>
                </tabbar-item>
                <tabbar-item link="/user">
                    <span slot="label">我的账号</span>
                </tabbar-item>
            </tabbar>
        </div>-->
        <!-- <div> -->
        <!-- <b-button @click="showModal" id="showBtn">Open Modal</b-button>
        <b-button @click="toggleModal" id="toggleBtn">Toggle Modal</b-button>-->
        <!-- <b-modal
                v-model="islogin"
                ref="myModalRef"
                centered
                lazy
                title="登陆"
                modal-ok="123123"
                modal-cancel="12312312"
            >
                <b-container fluid>
                    <b-row >
                        <b-col >
                            <b-form-input v-model="user.name" placeholder="请输入用户名"/>
                        </b-col>
                        
                    </b-row>
                    <b-row style="padding-top:20px">
                        <b-col>
                            <b-form-input :type="'password'" v-model="user.pwd" placeholder="请输入密码"/>
                        </b-col>
                    </b-row>
                </b-container>
                <div slot="modal-footer" class="w-100">
                    <b-button class="float-right" variant="success" @click="login">登陆</b-button>
        <!-- <b-button class="float-right" variant="primary">取消</b-button>-->
        <!-- </div> -->
        <!-- </b-modal> -->
        <!-- </div> -->
    </div>
</template>

<script>
import { XHeader, Tabbar, TabbarItem, Group, Cell } from "vux";

export default {
    name: "app",
    data() {
        return {
            islogin: true,
            user: {
                name: "",
                pwd: ""
            }
        };
    },
    components: {
        Tabbar,
        TabbarItem,
        Group,
        Cell,
        XHeader
    },
    methods: {
        showModal() {
            this.$refs.myModalRef.show();
        },
        hideModal() {
            this.$refs.myModalRef.hide();
        },
        toggleModal() {
            this.$refs.myModalRef.toggle("#toggleBtn");
        },
        cancel() {
            alert("calcel");
        },
        ok() {
            alert("calcel");
        },
        login() {
            this.login = false;
            this.$refs.myModalRef.hide();
            this.$vux.toast.text("登陆成功", "top");
            this.$router.push("/rank");
        }
    },
    created() {
        if (this.islogin == false) {
            this.showModal();
        }
    }
};
</script>

<style lang="less">
@import "~vux/src/styles/reset.less";
html,
body,
#app {
    height: 100%;
    width: 100%;
    overflow: auto;
    margin: 0;
    background-color: #fbf9fe;
    padding: 0;
}
</style>
