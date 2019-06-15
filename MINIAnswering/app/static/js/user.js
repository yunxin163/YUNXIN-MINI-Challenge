$(document).ajaxStart(function(){
    // 监听 Ajax 的进度条
    Pace.restart();
});
var router = function(config){

    this.initIndex = config.initIndex;
    this.events = config.events;

    this.onload = function(){
        if (window.location.hash == '') {
            window.location.href = `#/${this.initIndex}/`;
        } else {
            this.hadhash();
        }
        this.redrawRightRow();
        return true;
    };
    this.renderPage = function(args){
        // 完成基础动画
        $('html, body').animate({
            scrollTop: 0
        }, 250);
        $('#showcase').animate({
            opacity: 0
        }, 250);
        $('#left-row-1 .ul').removeClass('action');

        if (typeof args != 'object'){

            // 页内导航

            var id = args;

            $('#left-row-1 .ul[for="'+id+'"]').addClass('action');
            setTimeout(function(){
            	$('#showcase section.active').removeClass('active');
            	$('#showcase section[sectionID="'+id+'"]').addClass('active');
                $('#showcase').animate({
                    opacity: 1
                }, 180);
            }, 251);
            eval(`
                if(typeof(this.events.s${id}) != 'undefined'){
                    this.events.s${id}.call();
                }
                `);

        } else {

            // 页外导航
            var url = args.url,
                method = args.method,
                data = args.data || {};

            setTimeout(function(){
                $('#showcase section.active').removeClass('active');
                $.ajax({
                    url: url,
                    dataType: 'text',
                    type: method,
                    data: data,
                    success: function(html){
                        $('#ajax_page').html(html);
                        $('#ajax_page').addClass('active');
                        $('#showcase').animate({
                            opacity: 1
                        }, 180);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        userCenter.pushMsg('无法加载页面', 'alert');
                    }
                });
            }, 250);

        }
        return true;
    };

    this.renderPageByURL = function(url, method, data){
        var para = {
            m: method,
            u: url,
            d: data
        };
        window.location.href = '#/-1!@url=/'+encodeURIComponent(JSON.stringify(para));
    };
    this.leftBarShowOrHide = function(){
        $('#left-row').toggleClass('hide');
    };
    this.redrawRightRow = function () {
        var headerHeight = $('#header').height() + 8;
        $('#right-row').css({
            height: $(window).height() - headerHeight
        });
    };
    this.hadhash = function () {

        var h = window.location.hash,
            n = h.split('#/')[1],
            n = n.split('/')[0];
        if(n == '-1!@url='){
            // 页外链接
            var para = $.parseJSON(decodeURIComponent(h.split('#/' + n + '/')[1]));
            this.renderPage({
                url: para.u,
                method: para.m,
                data: para.d,
            });
        }else{
            // 页内导航
            this.renderPage(n);
        }

    };

    this.pushMsg = function(html, type){

        type = type || 'normal';
        var msg = $(`
            <div class="msg-box ${type}"">${html}</div>
        `);
        msg.animate({
            opacity: 1
        }, 180);
        msg.on('click', function(){
            $(this).animate({
                opacity: 0
            }, 250);
            setTimeout( () => {
                $(this).remove();
            }, 251);
        });
        $('#msg_container').append(msg);
        setTimeout(function(){
            msg.animate({
                opacity: 0
            }, 250);
            setTimeout(function(){
                msg.remove();
            }, 251);
        }, 5000);

    };
    this.popup = function(title, content, btn){

        var button = $(`<div class="btn"></div>`);
        for(i = 0; i < btn.length; i++){
            var defaultCls = (typeof(btn[i].isDefault) != 'undefined' && btn[i].isDefault == 1)? ' class="default"' : '';
            var btnEle = $(`<span${defaultCls}>${btn[i].text}</span>`);
            btnEle.on('click', btn[i].callback);
            button.append(btnEle);
        }
        var popup = $(`
            <div style="opacity: 0">
                <div class="popup-bg"></div>
                <div class="popup">
                    <div class="title">${title}</div>
                    <div class="main">${content}</div>
                </div>
            </div>
        `);
        $(popup[0]).children('.popup').append(button);
        $(popup[0]).children('.popup').children('.btn').children('span').on('click', function(){
            // 点击按钮退出
            $(this).parent().parent().parent().remove();
        });
        $(popup[0]).children('.popup-bg').on('click', function(){
            // 点击外部退出
            $(this).parent().remove();
        });
        $('#popup_container').append(popup);
        popup.animate({
            opacity: 1
        }, 100);
    };

};


$(document).ready(function(){
    // bind ul nav
    $('#left-row-1 .ul').on('click', function(){
    	var forID = parseInt($(this).attr('for'));
        window.location.href = '#/' + forID + '/';
    });
    $('#menu_button').on('click', function(){
    	userCenter.leftBarShowOrHide();
    });
});
$(window).bind('hashchange', function(){
	//监听hash变化
    userCenter.hadhash();
});

function randomNum(minNum,maxNum){ 
    switch(arguments.length){ 
        case 1: 
            return parseInt(Math.random()*minNum+1,10); 
        break; 
        case 2: 
            return parseInt(Math.random()*(maxNum-minNum+1)+minNum,10); 
        break; 
            default: 
                return 0; 
            break; 
    } 
}
function convertGenderToCN(gender){
    gender = parseInt(gender);
    switch(gender){
        case 0:
            return '男';
            break;
        case 1:
            return '女';
            break;
        default:
            return '其他';
    }
}
function getPosition(){
    return new Promise((resolve, reject) => {
        if(navigator.geolocation){
            navigator.geolocation.getCurrentPosition(function(position){
                let latitude = position.coords.latitude
                let longitude = position.coords.longitude
                let data = {
                    latitude: latitude,
                    longitude: longitude
                }
                resolve(data)
                }, function(){
                    reject(arguments)
                })
            }else{
                reject('你的浏览器不支持当前地理位置信息获取')
                userCenter.pushMsg('你的浏览器不支持当前地理位置信息获取', 'normal');
        }
    })
}
function timestampFormat(timestamp){
    function add0(m){
        return (m < 10)? '0' + m : m;
    }
    var time = new Date(timestamp),
        y = time.getFullYear(),
        m = time.getMonth() + 1,
        d = time.getDate(),
        h = time.getHours(),
        mm = time.getMinutes(),
        s = time.getSeconds();
    return y+'年'+add0(m)+'月'+add0(d)+'日';
}