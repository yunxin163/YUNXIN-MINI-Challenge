<template>
    <div :style="backgroundDiv" class="cls1">
        <div class="top">
            <h1>成绩分析系统</h1>
        </div>
        <div>
            <b-container fluid>
                <b-row style="margin-top:20px;">
                    <b-col sm="2" class="textc">
                        <label for="input-default">用户名</label>
                    </b-col>
                    <b-col sm="10">
                        <b-form-input :value="username" placeholder="请输入用户名"></b-form-input>
                    </b-col>
                </b-row>
                <b-row style="margin-top:20px;">
                    <b-col sm="2" class="textc">
                        <label for="input-default">密码</label>
                    </b-col>
                    <b-col sm="10">
                        <b-form-input type="password" :value="password" placeholder="请输入密码"></b-form-input>
                    </b-col>
                </b-row>
            </b-container>
        </div>
        <x-button type="primary" @click.native="login()" style="width:400px;margin-top:20px;">登陆</x-button>
        <!-- <group>
            <x-input type="text" placeholder="请输入用户名" :value="username"></x-input>
            <x-input type="password" placeholder="请输入密码" :value="password"></x-input>
        </group>-->
    </div>
</template>
<script>
import { XInput, Group, Toast, XButton } from "vux";
export default {
    components: {
        XInput,
        Group,
        Toast,
        XButton
    },
    data() {
        return {
            username: "",
            password: "",
            backgroundDiv: {
                backgroundImage:
                    "url(https://www.gaoguang.com/uploads/allimg/180719/2-1PG9163952.jpg)"
            }
        };
    },
    methods: {
        login() {
            const _this = this;
            _this.$axios
                .post(
                    "https://easy-mock.com/mock/5ca462db5eeed03805bf48f7/yhc/user/login"
                )
                .then(res => {
                    if (res.data.code == 200) {
                        this.$vux.toast.show({
                            text: `欢迎登陆 ${_this.username}`
                        });
                        _this.$router.replace({ path: "/main" });
                    }
                })
                .catch(err => {
                    console.log(err);
                });
        }
    }
};
</script>
<style scoped>
.cls1 {
    width: 100%;
    height: 100%;
    overflow: hidden;
    margin: 0;
    padding-bottom: 100%;
}
.top {
    /* background-color: aquamarine; */
    background: rgba(127, 255, 212, 0.3);
    /* color: aquamarine; */
    /* opacity: 0.3; */
    height: 100px;
    color: blanchedalmond;
    justify-content: center;
    margin-top: 50px;
    display: flex;
    align-items: center;
}
.textc {
    color: blanchedalmond;
}
</style>
