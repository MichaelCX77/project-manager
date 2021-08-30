// jQuery Mask Plugin v1.14.16
// github.com/igorescobar/jQuery-Mask-Plugin
var $jscomp=$jscomp||{};$jscomp.scope={};$jscomp.findInternal=function(a,n,f){a instanceof String&&(a=String(a));for(var p=a.length,k=0;k<p;k++){var b=a[k];if(n.call(f,b,k,a))return{i:k,v:b}}return{i:-1,v:void 0}};$jscomp.ASSUME_ES5=!1;$jscomp.ASSUME_NO_NATIVE_MAP=!1;$jscomp.ASSUME_NO_NATIVE_SET=!1;$jscomp.SIMPLE_FROUND_POLYFILL=!1;
$jscomp.defineProperty=$jscomp.ASSUME_ES5||"function"==typeof Object.defineProperties?Object.defineProperty:function(a,n,f){a!=Array.prototype&&a!=Object.prototype&&(a[n]=f.value)};$jscomp.getGlobal=function(a){return"undefined"!=typeof window&&window===a?a:"undefined"!=typeof global&&null!=global?global:a};$jscomp.global=$jscomp.getGlobal(this);
$jscomp.polyfill=function(a,n,f,p){if(n){f=$jscomp.global;a=a.split(".");for(p=0;p<a.length-1;p++){var k=a[p];k in f||(f[k]={});f=f[k]}a=a[a.length-1];p=f[a];n=n(p);n!=p&&null!=n&&$jscomp.defineProperty(f,a,{configurable:!0,writable:!0,value:n})}};$jscomp.polyfill("Array.prototype.find",function(a){return a?a:function(a,f){return $jscomp.findInternal(this,a,f).v}},"es6","es3");
(function(a,n,f){"function"===typeof define&&define.amd?define(["jquery"],a):"object"===typeof exports&&"undefined"===typeof Meteor?module.exports=a(require("jquery")):a(n||f)})(function(a){var n=function(b,d,e){var c={invalid:[],getCaret:function(){try{var a=0,r=b.get(0),h=document.selection,d=r.selectionStart;if(h&&-1===navigator.appVersion.indexOf("MSIE 10")){var e=h.createRange();e.moveStart("character",-c.val().length);a=e.text.length}else if(d||"0"===d)a=d;return a}catch(C){}},setCaret:function(a){try{if(b.is(":focus")){var c=
b.get(0);if(c.setSelectionRange)c.setSelectionRange(a,a);else{var g=c.createTextRange();g.collapse(!0);g.moveEnd("character",a);g.moveStart("character",a);g.select()}}}catch(B){}},events:function(){b.on("keydown.mask",function(a){b.data("mask-keycode",a.keyCode||a.which);b.data("mask-previus-value",b.val());b.data("mask-previus-caret-pos",c.getCaret());c.maskDigitPosMapOld=c.maskDigitPosMap}).on(a.jMaskGlobals.useInput?"input.mask":"keyup.mask",c.behaviour).on("paste.mask drop.mask",function(){setTimeout(function(){b.keydown().keyup()},
100)}).on("change.mask",function(){b.data("changed",!0)}).on("blur.mask",function(){f===c.val()||b.data("changed")||b.trigger("change");b.data("changed",!1)}).on("blur.mask",function(){f=c.val()}).on("focus.mask",function(b){!0===e.selectOnFocus&&a(b.target).select()}).on("focusout.mask",function(){e.clearIfNotMatch&&!k.test(c.val())&&c.val("")})},getRegexMask:function(){for(var a=[],b,c,e,t,f=0;f<d.length;f++)(b=l.translation[d.charAt(f)])?(c=b.pattern.toString().replace(/.{1}$|^.{1}/g,""),e=b.optional,
(b=b.recursive)?(a.push(d.charAt(f)),t={digit:d.charAt(f),pattern:c}):a.push(e||b?c+"?":c)):a.push(d.charAt(f).replace(/[-\/\\^$*+?.()|[\]{}]/g,"\\$&"));a=a.join("");t&&(a=a.replace(new RegExp("("+t.digit+"(.*"+t.digit+")?)"),"($1)?").replace(new RegExp(t.digit,"g"),t.pattern));return new RegExp(a)},destroyEvents:function(){b.off("input keydown keyup paste drop blur focusout ".split(" ").join(".mask "))},val:function(a){var c=b.is("input")?"val":"text";if(0<arguments.length){if(b[c]()!==a)b[c](a);
c=b}else c=b[c]();return c},calculateCaretPosition:function(a){var d=c.getMasked(),h=c.getCaret();if(a!==d){var e=b.data("mask-previus-caret-pos")||0;d=d.length;var g=a.length,f=a=0,l=0,k=0,m;for(m=h;m<d&&c.maskDigitPosMap[m];m++)f++;for(m=h-1;0<=m&&c.maskDigitPosMap[m];m--)a++;for(m=h-1;0<=m;m--)c.maskDigitPosMap[m]&&l++;for(m=e-1;0<=m;m--)c.maskDigitPosMapOld[m]&&k++;h>g?h=10*d:e>=h&&e!==g?c.maskDigitPosMapOld[h]||(e=h,h=h-(k-l)-a,c.maskDigitPosMap[h]&&(h=e)):h>e&&(h=h+(l-k)+f)}return h},behaviour:function(d){d=
d||window.event;c.invalid=[];var e=b.data("mask-keycode");if(-1===a.inArray(e,l.byPassKeys)){e=c.getMasked();var h=c.getCaret(),g=b.data("mask-previus-value")||"";setTimeout(function(){c.setCaret(c.calculateCaretPosition(g))},a.jMaskGlobals.keyStrokeCompensation);c.val(e);c.setCaret(h);return c.callbacks(d)}},getMasked:function(a,b){var h=[],f=void 0===b?c.val():b+"",g=0,k=d.length,n=0,p=f.length,m=1,r="push",u=-1,w=0;b=[];if(e.reverse){r="unshift";m=-1;var x=0;g=k-1;n=p-1;var A=function(){return-1<
g&&-1<n}}else x=k-1,A=function(){return g<k&&n<p};for(var z;A();){var y=d.charAt(g),v=f.charAt(n),q=l.translation[y];if(q)v.match(q.pattern)?(h[r](v),q.recursive&&(-1===u?u=g:g===x&&g!==u&&(g=u-m),x===u&&(g-=m)),g+=m):v===z?(w--,z=void 0):q.optional?(g+=m,n-=m):q.fallback?(h[r](q.fallback),g+=m,n-=m):c.invalid.push({p:n,v:v,e:q.pattern}),n+=m;else{if(!a)h[r](y);v===y?(b.push(n),n+=m):(z=y,b.push(n+w),w++);g+=m}}a=d.charAt(x);k!==p+1||l.translation[a]||h.push(a);h=h.join("");c.mapMaskdigitPositions(h,
b,p);return h},mapMaskdigitPositions:function(a,b,d){a=e.reverse?a.length-d:0;c.maskDigitPosMap={};for(d=0;d<b.length;d++)c.maskDigitPosMap[b[d]+a]=1},callbacks:function(a){var g=c.val(),h=g!==f,k=[g,a,b,e],l=function(a,b,c){"function"===typeof e[a]&&b&&e[a].apply(this,c)};l("onChange",!0===h,k);l("onKeyPress",!0===h,k);l("onComplete",g.length===d.length,k);l("onInvalid",0<c.invalid.length,[g,a,b,c.invalid,e])}};b=a(b);var l=this,f=c.val(),k;d="function"===typeof d?d(c.val(),void 0,b,e):d;l.mask=
d;l.options=e;l.remove=function(){var a=c.getCaret();l.options.placeholder&&b.removeAttr("placeholder");b.data("mask-maxlength")&&b.removeAttr("maxlength");c.destroyEvents();c.val(l.getCleanVal());c.setCaret(a);return b};l.getCleanVal=function(){return c.getMasked(!0)};l.getMaskedVal=function(a){return c.getMasked(!1,a)};l.init=function(g){g=g||!1;e=e||{};l.clearIfNotMatch=a.jMaskGlobals.clearIfNotMatch;l.byPassKeys=a.jMaskGlobals.byPassKeys;l.translation=a.extend({},a.jMaskGlobals.translation,e.translation);
l=a.extend(!0,{},l,e);k=c.getRegexMask();if(g)c.events(),c.val(c.getMasked());else{e.placeholder&&b.attr("placeholder",e.placeholder);b.data("mask")&&b.attr("autocomplete","off");g=0;for(var f=!0;g<d.length;g++){var h=l.translation[d.charAt(g)];if(h&&h.recursive){f=!1;break}}f&&b.attr("maxlength",d.length).data("mask-maxlength",!0);c.destroyEvents();c.events();g=c.getCaret();c.val(c.getMasked());c.setCaret(g)}};l.init(!b.is("input"))};a.maskWatchers={};var f=function(){var b=a(this),d={},e=b.attr("data-mask");
b.attr("data-mask-reverse")&&(d.reverse=!0);b.attr("data-mask-clearifnotmatch")&&(d.clearIfNotMatch=!0);"true"===b.attr("data-mask-selectonfocus")&&(d.selectOnFocus=!0);if(p(b,e,d))return b.data("mask",new n(this,e,d))},p=function(b,d,e){e=e||{};var c=a(b).data("mask"),f=JSON.stringify;b=a(b).val()||a(b).text();try{return"function"===typeof d&&(d=d(b)),"object"!==typeof c||f(c.options)!==f(e)||c.mask!==d}catch(w){}},k=function(a){var b=document.createElement("div");a="on"+a;var e=a in b;e||(b.setAttribute(a,
"return;"),e="function"===typeof b[a]);return e};a.fn.mask=function(b,d){d=d||{};var e=this.selector,c=a.jMaskGlobals,f=c.watchInterval;c=d.watchInputs||c.watchInputs;var k=function(){if(p(this,b,d))return a(this).data("mask",new n(this,b,d))};a(this).each(k);e&&""!==e&&c&&(clearInterval(a.maskWatchers[e]),a.maskWatchers[e]=setInterval(function(){a(document).find(e).each(k)},f));return this};a.fn.masked=function(a){return this.data("mask").getMaskedVal(a)};a.fn.unmask=function(){clearInterval(a.maskWatchers[this.selector]);
delete a.maskWatchers[this.selector];return this.each(function(){var b=a(this).data("mask");b&&b.remove().removeData("mask")})};a.fn.cleanVal=function(){return this.data("mask").getCleanVal()};a.applyDataMask=function(b){b=b||a.jMaskGlobals.maskElements;(b instanceof a?b:a(b)).filter(a.jMaskGlobals.dataMaskAttr).each(f)};k={maskElements:"input,td,span,div",dataMaskAttr:"*[data-mask]",dataMask:!0,watchInterval:300,watchInputs:!0,keyStrokeCompensation:10,useInput:!/Chrome\/[2-4][0-9]|SamsungBrowser/.test(window.navigator.userAgent)&&
k("input"),watchDataMask:!1,byPassKeys:[9,16,17,18,36,37,38,39,40,91],translation:{0:{pattern:/\d/},9:{pattern:/\d/,optional:!0},"#":{pattern:/\d/,recursive:!0},A:{pattern:/[a-zA-Z0-9]/},S:{pattern:/[a-zA-Z]/}}};a.jMaskGlobals=a.jMaskGlobals||{};k=a.jMaskGlobals=a.extend(!0,{},k,a.jMaskGlobals);k.dataMask&&a.applyDataMask();setInterval(function(){a.jMaskGlobals.watchDataMask&&a.applyDataMask()},k.watchInterval)},window.jQuery,window.Zepto);

$(document).ready(function(){
    var app = {
        init: function() {
            app.toggleMenu();
            app.selectPagesRequest();
            app.formValidations();
            app.cepAutoComplete();
            app.addMasks();
            app.homeCalendar();
            app.accountSlider();
        },
        toggleMenu: function() {
            $("main").prepend("<div class='backdrop'></div>")
    
            $(".menu-icon .burger").on("click", function () {
                if($(".box-menu").hasClass("is-open")) {
                    $(".box-menu, .backdrop").removeClass("is-open")
                } else {
                    $(".aside-menu-bar").removeClass("is-open")
                    $(".box-menu, .backdrop").addClass("is-open")
                }
            })
            
            $(".menu-icon .config").on("click", function () {
                if($(".aside-menu-bar").hasClass("is-open")) {
                    $(".aside-menu-bar, .backdrop").removeClass("is-open")
                } else {
                    $(".box-menu").removeClass("is-open")
                    $(".aside-menu-bar, .backdrop").addClass("is-open")
                }
            })

            $(".backdrop").on("click", function () {
                $(".box-menu, .aside-menu-bar, .backdrop").removeClass("is-open")
            })            
        },
        selectPagesRequest: function() {
            $("#actions-select").on("change", function(){
                var optionValue = $(this).val();
                window.location.pathname = "/" + optionValue
            })
        },
        formValidations: function() {
            function validEmail(email) {
                var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return re.test(email);
            }

            function validCpf(cpf) {
                var Soma, Resto;                
                Soma = 0;
                cpf = cpf.replace(/\./g,"").replace("-","")
                if (cpf == "00000000000" || cpf == "11111111111" || cpf == "22222222222" || cpf == "33333333333" || cpf == "44444444444" || cpf == "55555555555" || cpf == "66666666666" || cpf == "77777777777" || cpf == "88888888888" || cpf == "99999999999" || cpf.length != 11) return false;
                        
                for (i=1; i<=9; i++) Soma = Soma + parseInt(cpf.substring(i-1, i)) * (11 - i);
                Resto = (Soma * 10) % 11;
                    
                if ((Resto == 10) || (Resto == 11))  Resto = 0;
                if (Resto != parseInt(cpf.substring(9, 10)) ) return false;
                
                Soma = 0;
                for (i = 1; i <= 10; i++) Soma = Soma + parseInt(cpf.substring(i-1, i)) * (12 - i);
                Resto = (Soma * 10) % 11;
                    
                if ((Resto == 10) || (Resto == 11))  Resto = 0;
                if (Resto != parseInt(cpf.substring(10, 11) ) ) return false;
                
                return true;
            }

            function validCnpj(cnpj) {
                var cnpj = cnpj.replace(/[^\d]+/g, '')

                // Valida a quantidade de caracteres
                if (cnpj.length !== 14)
                    return false
            
                // Elimina inválidos com todos os caracteres iguais
                if (/^(\d)\1+$/.test(cnpj))
                    return false
            
                // Cáculo de validação
                let t = cnpj.length - 2,
                    d = cnpj.substring(t),
                    d1 = parseInt(d.charAt(0)),
                    d2 = parseInt(d.charAt(1)),
                    calc = x => {
                        let n = cnpj.substring(0, x),
                            y = x - 7,
                            s = 0,
                            r = 0
            
                            for (let i = x; i >= 1; i--) {
                                s += n.charAt(x - i) * y--;
                                if (y < 2)
                                    y = 9
                            }

                            r = 11 - s % 11
                            return r > 9 ? 0 : r
                    }
                return calc(t) === d1 && calc(t + 1) === d2
            }

            function validateAcceptCheckbox() {    
                if(!$(".input-accept").length) return  
                if($(".input-accept input").prop("checked")) {
                    $(".accept-message.error-message").remove();
                } else {
                    if(!$(".accept-message.error-message").length) $(".form-cadastro .input-accept").append("<small class='accept-message error-message'>Por favor, Aceite nossos termos</small>")
                }
            }

            function validatePasswordConfirmation() {
                if(!$("#input-conf-senha").length) return 
                if($("#input-senha").val() == $("#input-conf-senha").val()) {
                    $(".unmatch-password.error-message").remove();
                } else {
                    if(!$(".unmatch-password.error-message").length) $("#input-conf-senha").after("<small class='unmatch-password error-message'>As senhas não coincidem</small>")
                }
            }
            
            function validateFieldText(field){
                if(field.val() == "") {
                    if(!field.parent().find(".error-message").length) 
                        field.after("<small class='error-message'>Por favor, preencha este campo.</small>")
                } else {
                    field.parent().find(".error-message").remove();
                }
            }
            
            function validateFieldEmail(field){
                if(validEmail(field.val())) {
                    field.parent().find(".error-message").remove();
                } else {
                    if(!field.parent().find(".error-message").length)
                        field.after("<small class='error-message'>Email inválido.</small>")
                }
            }
            
            function validateFieldCpf(field){
                if(validCpf(field.val())) {
                    field.parent().find(".error-message").remove();
                } else {
                    if(!field.parent().find(".error-message").length)
                        field.after("<small class='error-message'>Cpf Inválido..</small>");
                }
            }

            function validateFieldCnpj(field){
                if(validCnpj(field.val())) {
                    field.parent().find(".error-message").remove();
                } else {
                    if(!field.parent().find(".error-message").length)
                        field.after("<small class='error-message'>Cnpj Inválido..</small>");
                }
            }            

            function generalValidate(field){
                if(field.attr("type") == "text" || field.attr("type") == "date" || field.attr("type") == "number" || field.attr("type") == "password") {
                    validateFieldText(field)
                }

                if(field.attr("type") == "email") {
                    validateFieldEmail(field)
                }

                if(field.attr("id") == "input-cpf") {
                    validateFieldCpf(field)
                }

                if(field.attr("id") == "cnpj-field") {
                    validateFieldCnpj(field)
                }

                if(field.attr("id") == "input-conf-senha") {
                    validatePasswordConfirmation();
                }
            }
            
            var form = $(".form-cadastro");                
            var formFields = $(".form-cadastro input");                                

            formFields.on("blur", function(){
                generalValidate($(this))
            })

            $(".input-accept span").on("click",function(){             
                var check = $(this).prev().prop("checked");
                check ? $(this).prev().prop("checked", false) : $(this).prev().prop("checked", true) 
            })

            $(".form-cadastro .form-btn").on("click",function(e){
                e.preventDefault()                   

                formFields.each(function(){
                    generalValidate($(this))
                }) 

                validateAcceptCheckbox();
                validatePasswordConfirmation();

                console.log($(".error-message").length)
                if(!$(".error-message").length) form.submit()
            })
        },
        cepAutoComplete: function() {
            function limpa_formulário_cep() {
                $("#address-field").val("");
                $("#city-field").val("");
                $("#state-field").val("");
                $("#number-field").val("");
            }
            
            //Quando o campo cep perde o foco.
            $("#cep-field").blur(function() {
        
                //Nova variável "cep" somente com dígitos.
                var cep = $(this).val().replace(/\D/g, '');
        
                //Verifica se campo cep possui valor informado.
                if (cep != "") {
        
                    //Expressão regular para validar o CEP.
                    var validacep = /^[0-9]{8}$/;
        
                    //Valida o formato do CEP.
                    if(validacep.test(cep)) {
        
                        //Preenche os campos com "..." enquanto consulta webservice.
                        $("#address-field").val("...");
                        $("#city-field").val("...");
                        $("#state-field").val("...");
        
                        //Consulta o webservice viacep.com.br/
                        $.getJSON("https://viacep.com.br/ws/"+ cep +"/json/?callback=?", function(dados) {
                        console.log(dados)
                            if (!("erro" in dados)) {
                                //Atualiza os campos com os valores da consulta.
                                
                                $("#address-field").val(dados.logradouro + ", " + dados.bairro).trigger("blur");
                                $("#city-field").val(dados.localidade).trigger("blur");
                                $("#state-field").val(dados.uf).trigger("blur");
                            } //end if.
                            else {
                                //CEP pesquisado não foi encontrado.
                                limpa_formulário_cep();
                                alert("CEP não encontrado.");
                            }
                        });
                    } //end if.
                    else {
                        //cep é inválido.
                        limpa_formulário_cep();
                        alert("Formato de CEP inválido.");
                    }
                } //end if.
                else {
                    //cep sem valor, limpa formulário.
                    limpa_formulário_cep();
                }
            });
        },
        addMasks: function() {
            $("#cep-field").mask("05422-030");
            $("#input-cpf").mask("000.000.000-00")
            $("#cnpj-field").mask('00.000.000/0000-00');
            $("#director-phone").mask('(00) 00000-0000');
        },
        homeCalendar: function() {
            if(!$("body").hasClass("home")) return
            var configObject = {
                startOfWeek: 'monday',
                separator : ' ~ ',
                format: 'DD.MM.YYYY HH:mm',
                autoClose: false,
                time: {
                    enabled: true
                }
            }
            $('.home-content').dateRangePicker(configObject)
            $(".home-content").append($(".date-picker-wrapper"))
            $(".date-picker-wrapper").show()

            $("body.home .home-list-items .list-item > a").on("click", function(){
                var startDate = $(this).find(".box-date p").first().text();
                var finalDate = $(this).find(".box-date p").last().text();
            
                $('.home-content').data('dateRangePicker').setDateRange(startDate,finalDate);
            })
        },
        accountSlider: function() {
            $("body.perfil-page .lista-indicacoes").slick({
                dots: true,
                arrows: false,
                slidesToShow: 1,
                touchMove: true
            });
        }
    }
    app.init();
})