// file app.js

define(['view/LoginView', 'view/PagesView', 'view/IndexView', 
        'text!template/topbar.html', 'text!template/locale.html'], 
function(LoginView, PagesView, IndexView, topbarTmpl, localeTmpl){
	
	console.log("app.js");

	return Backbone.Router.extend({
		
		routes: {
			'index': 		'index',
			'pages': 	'pages'
		},
		
		currentView: null,
		indexView: new IndexView(),
		loginView: new LoginView(),
		pagesView: new PagesView(),
		
		show: function(view) {
			if (this.currentView) {
				this.currentView.remove();
				this.currentView = null;
			}
			view.render();
			this.currentView = view;
		},
		
		pages: function() {
			this.show(this.pagesView);
		},
		
		index: function() {
			this.show(this.indexView);
		},

		initialize:function() {
			this.bind("login", this.login, this);

			this.currentView = this.loginView.render();
			$('#loading').html(messages('loading'));
		},
		
		login: function() {
			Vosao.jsonrpcInitialized = false;
			Vosao.createJSONRpc();
			Vosao.initJSONRpc(function() {
				Vosao.jsonrpc.userService.getLoggedIn(function(user) {
					Vosao.app.user = user;

					var localeHtml = _.template(localeTmpl, {messages:messages});
					$('#topbar').html(_.template(topbarTmpl, 
						{locale: localeHtml, "Vosao": Vosao, "messages": messages}
					));
					$('#languageSelect').click(function() {
						$('#languageDiv').show();
						setTimeout(function() {
					        $('#languageDiv').hide();
						}, 5000);
					});

					Backbone.history.start();
					Vosao.app.navigate('index', true);
				});
			});
		}
	});

});