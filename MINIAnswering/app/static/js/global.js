var $window = $(window);
$window.scroll(function(){
    var scrollTop = $window.scrollTop();
    if (scrollTop > 0) {
        if ($('#home-top-nav').hasClass('show-bg') == false) {
            $('#home-top-nav').addClass('show-bg');
        }
    } else {
        if ($('#home-top-nav').hasClass('show-bg')) {
            $('#home-top-nav').removeClass('show-bg');
        }
    }
});
function basic_input_wrong(el, msg){
    var msgbox = $(`<div class="basic-input-wrong-msgbox">${msg}</div>`);
    msgbox.css({
        left: el.offset().left + 'px',
        width: (el.width() - 20) + 'px'
    });
    el.addClass('wrong');
    el.after(msgbox);
    msgbox.show(400);
    setTimeout(function(){
        msgbox.hide(350);
        el.removeClass('wrong');
    }, 5000);
}
function convert_FormData_to_json(formData){
    var objData = {};
    for(var entry of formData.entries()){
        objData[entry[0]] = entry[1];
    }
    return objData;
}
// Basic UI
// CHIPS BEGIN
var ui_chips = function(query) {
    this.el = document.querySelector(query);
    this.options = this.el.querySelectorAll('.chip');
    this.getDataByText = function() {
        var chips = this.el.querySelectorAll('.chip.active'),
            data = [];
        for (i = 0; i < chips.length; i++) {
            data.push(chips[i].innerText);
        }
        return data;
    };
    this.getDataByValue = function() {
        var chips = this.el.querySelectorAll('.chip.active'),
            data = [];
        for (i = 0; i < chips.length; i++) {
            data.push(chips[i].dataset.value);
        }
        return data;
    };

    // Init
    for (i = 0; i < this.options.length; i++){
        this.options[i].onclick = function() {
            $(this).toggleClass('active');
        };
    }
};
// 关注问题
function followQuestion(questionID, callback) {
    $.post('/question/follow/', {question_id: questionID}, function (d) {
        var result = (d.states == 200);
        if (typeof callback != 'undefined' && callback != null){
            callback(result);
        }
    });
}
// 取消关注问题
function unfollowQuestion(questionID, callback) {
    $.post('/question/unfollow/', {question_id: questionID}, function (d) {
        var result = (d.states == 200);
        if (typeof callback != 'undefined' && callback != null){
            callback(result);
        }
    });
}
