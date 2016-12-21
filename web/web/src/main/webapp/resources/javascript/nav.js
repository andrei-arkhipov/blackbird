jQuery(document).ready(function($) {
	var pattern = '';
	if (window.location.href.indexOf('catalog', 0) != -1) {
		pattern = 'catalog';
	}
	if (window.location.href.indexOf('eventlog', 0) != -1) {
		pattern = 'eventlog';
	}
	if (window.location.href.indexOf('logs', 0) != -1) {
		pattern = 'logs';
	}
	if (window.location.href.indexOf('reports', 0) != -1) {
		pattern = 'reports';
	}
	if (window.location.href.indexOf('admin', 0) != -1) {
		pattern = 'admin';
	}
	if (pattern != '') {
		$('#site-nav').find('a').each(function(key, elem) {
			if ($(elem).attr('href').indexOf(pattern, 0) != -1) {
				$(elem).addClass('active');
				return;
			}
		});
	}
});