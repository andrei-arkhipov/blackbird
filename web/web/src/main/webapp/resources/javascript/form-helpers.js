PrimeFaces.locales ['ru'] = {
                closeText: 'Закрыть',
                prevText: 'Назад',
                nextText: 'Вперёд',
                currentText: 'Home',
                monthNames: ['Январь', 'Февраль' , 'Март' , 'Апрель' , 'Май' , 'Июнь' , 'Июль' , 'Август' , 'Сентябрь','Октябрь','Ноябрь','Декабрь' ],
                monthNamesShort: ['Янв', 'Фев', 'Мар', 'Апр', 'Май', 'Июн', 'Июл', 'Авг', 'Сен', 'Окт', 'Ноя', 'Дек' ],
                dayNames: ['Воскресенье', 'Понедельник', 'Вторник', 'Среда', 'Четверг', 'Пятница', 'Субота'],
                dayNamesShort: ['Воск','Пон' , 'Вт' , 'Ср' , 'Четв' , 'Пят' , 'Суб'],
                dayNamesMin: ['В', 'П', 'Вт', 'С ', 'Ч', 'П ', 'Сб'],
                weekHeader: 'Неделя',
                FirstDay: 1,
                isRTL: false,
                showMonthAfterYear: false,
                yearSuffix:'',
                timeOnlyTitle: 'Только время',
                timeText: 'Время',
                hourText: 'Час',
                minuteText: 'Минута',
                secondText: 'Секунда',
                currentText: 'Сегодня',
                ampm: false,
                month: 'Месяц',
                week: 'неделя',
                day: 'День',
                allDayText: 'Весь день'
            };

jQuery(document).ready(function($) {
	$('input').live('blur', function() {
		$(this).val($.trim($(this).val()));
	});
});

jQuery(document).ready(function($) {
	   textboxes = $("input:text");
	   textboxes2 = $("input.modalSearch");

	   if ($.browser.mozilla) {
	      $(textboxes).keypress(checkForEnter);
	      
	   } else {
	      $(textboxes).keydown(checkForEnter);
	      
	   }

	   function checkForEnter(event) {
	      if (event.keyCode == 13) {
	         currentTextboxNumber = textboxes.index(this);

//	         if (textboxes[currentTextboxNumber + 1] != null) {
//	           nextTextbox = textboxes[currentTextboxNumber + 1];
//	           nextTextbox.select();
//	        }

	         event.preventDefault();
	         return false;
	      }
	   }
	   
	   function fireModalWindow(event){
		   if( event.keyCode==13){ 
				$(this).next().trigger("click");
				event.preventDefault(); 
				return false;	
	       }
	   }
	});

function markReadable(elem) {
	if ($(elem).attr('checked') == 'checked') {
		$(elem).parent().parent().prev().find('input').attr('checked', 'checked');
	}
}

function markAll(elem, holder,selector){
	
}

function disableEnterKey(e)
{
     var key;
     if(window.event)
          key = window.event.keyCode;     //IE
     else
          key = e.which;     //firefox
     if(key == 13)
          return false;
     else
          return true;
}

function arrowKeys(event, elementRef) {
	var radio = $(elementRef);
	if ((event.which > 36 && event.which < 41)
			|| (event.keyCode > 36 && event.keyCode < 41)) {
		if (event.preventDefault) {
			event.preventDefault();
		} else {
			event.returnValue = false;
		}
		if (event.stopPropagation) {
			event.stopPropagation();
		} else {
			event.cancelBubble = true;
		}

		if (event.which == 37 || event.keyCode == 37) {// left
			radio.parent().parent().prev().find('input[type="radio"]').focus();
		}
		if (event.which == 38 || event.keyCode == 38) {// up
			radio.parent().parent().prev().find('input[type="radio"]').focus();
		}
		if (event.which == 39 || event.keyCode == 39) {// right
			radio.parent().parent().next().find('input[type="radio"]').focus();
		}
		if (event.which == 40 || event.keyCode == 40) {// down
			radio.parent().parent().next().find('input[type="radio"]').focus();
		}
		return false;
	}
	if (event.which == 13 || event.keyCode == 13) {
		radio.attr("checked", "checked");
		radio.trigger('click');
		if (event.preventDefault) {
			event.preventDefault();
		} else {
			event.returnValue = false;
		}
		if (event.stopPropagation) {
			event.stopPropagation();
		} else {
			event.cancelBubble = true;
		}
		return false;
	}
	if ((event.which == 9 || event.keyCode == 9) && !event.shiftKey) {
		radio.parent().parent().next().find('input[type="radio"]').focus();
		if (event.preventDefault) {
			event.preventDefault();
		} else {
			event.returnValue = false;
		}
		if (event.stopPropagation) {
			event.stopPropagation();
		} else {
			event.cancelBubble = true;
		}
		return false;
	}
}

function disableNonLatinKeys(e,exist)
{
    var unicode=e.keyCode;
    if (navigator.userAgent.indexOf("Firefox") > 0)
        unicode = e.which; // (standard method)
    else 
        unicode = e.keyCode;
    if((unicode < 0x30 && unicode != 0x2D && unicode != 0x2E && unicode != 0x08 && unicode != 0x09 ) || (unicode > 0x7A && unicode != 0x7F) || unicode == 0x5B || unicode == 0x5B || unicode == 0x5C || unicode == 0x5D || unicode == 0x5E || unicode == 0x60) {
        growl.renderMessage({summary:'Ошибка', detail: 'Только латинские символы, подчеркивание, тире и точку!', severity: 0});
        return false;
    }
    else {
        return true;
    }
}