var searchHelper = {
		
		cdocSetClient : function(data){
            $($($('.ui-fieldset')[2]).find('table').find('tr:nth-of-type(1)')[0]).val(data.lastName);
            $($($('.ui-fieldset')[2]).find('table').find('tr:nth-of-type(2)')[0]).val(data.firstName);
            $($($('.ui-fieldset')[2]).find('table').find('tr:nth-of-type(3)')[0]).val(data.middleName);
//            $('#fill-form\\:lastNameId').val(data.lastName);
//            $('#fill-form\\:firstNameId').val(data.firstName);
//            $('#fill-form\\:middleNameId').val(data.middleName);
			console.log(data);
			dlg1.hide();
		}
		
}