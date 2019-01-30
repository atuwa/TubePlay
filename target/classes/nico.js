var _slicedToArray = function () {
		function sliceIterator(arr, i) {
			var _arr = [];
			var _n = true;
			var _d = false;
			var _e = undefined;
			try {
				for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) {
					_arr.push(_s.value);
					if (i && _arr.length === i) break;
				}
			}catch(err){
				_d = true;
				_e = err;
			}finally{
				try{
					if (!_n && _i["return"]) _i["return"]();
				}finally{
					if (_d) throw _e;
				}
			}
			return _arr;
			}
		return function (arr, i){
			if (Array.isArray(arr)){
				return arr;
			}else if(Symbol.iterator in Object(arr)){
				return sliceIterator(arr, i);
			}else{
				throw new TypeError("Invalid attempt to destructure non-iterable instance");
			}
		};
	}();
	var _createClass = function () {
		function defineProperties(target, props) {
			for (var i = 0; i < props.length; i++) {
				var descriptor = props[i];
				descriptor.enumerable = descriptor.enumerable || false;
				descriptor.configurable = true;
				if ("value" in descriptor) descriptor.writable = true;
				Object.defineProperty(target, descriptor.key, descriptor);
				}
			}
		return function (Constructor, protoProps, staticProps) {
			if (protoProps) defineProperties(Constructor.prototype, protoProps);
			if (staticProps) defineProperties(Constructor, staticProps);
			return Constructor;
		};
	}();
	function _classCallCheck(instance, Constructor) {
		if (!(instance instanceof Constructor)) {
			throw new TypeError("Cannot call a class as a function");
			}
		}
	var NicovideoPlayer = function () {
			function NicovideoPlayer(containerSelector, watchId) {_classCallCheck(this, NicovideoPlayer);
		    this.playerId = (++NicovideoPlayer.playerId).toString();
		    this.container = document.querySelector(containerSelector);
		    this.watchId = watchId;
		    this.state = {
		      isRepeat: false };


		    this.messageListener();
		    this.render();
		  }_createClass(NicovideoPlayer, [{ key: 'render', value: function render()

		    {
		      var wrapper = document.createElement('div');
		      wrapper.classList.add('c-nicovideoPlayer--' + this.playerId);

		      this.player = this.renderPlayer();
		      this.controls = this.renderControls();

		      wrapper.appendChild(this.player);
		      wrapper.appendChild(this.controls);

		      this.container.appendChild(wrapper);
		    } }, { key: 'renderInfoTable', value: function renderInfoTable(

		    videoInfo) {
		      var table = document.createElement('table');
		      var tbody = document.createElement('tbody');
		      table.classList.add('c-infoTable');

		      Object.entries(videoInfo).forEach(function (_ref) {var _ref2 = _slicedToArray(_ref, 2),key = _ref2[0],value = _ref2[1];
		        var tr = document.createElement('tr');
		        var th = document.createElement('th');
		        var td = document.createElement('td');

		        th.innerText = key.toString();
		        td.innerHTML = value.toString();

		        tr.appendChild(th);
		        tr.appendChild(td);
		        tbody.appendChild(tr);
		      });

		      table.appendChild(tbody);
		      this.player.parentElement.appendChild(table);
		    } }, { key: 'renderPlayer', value: function renderPlayer()

		    {
		      var player = document.createElement('iframe');
		      var source = new URL(NicovideoPlayer.origin + '/watch/' + this.watchId);
		      var params = {
		        jsapi: 1,
		        playerId: this.playerId };


		      Object.entries(params).forEach(function (_ref3) {var _ref4 = _slicedToArray(_ref3, 2),key = _ref4[0],value = _ref4[1];return (
		          source.searchParams.append(key, value));});


		      player.width = 480;
		      player.height = 259;
		      player.src = source;
		      player.frameBorder = 0;
		      player.allowFullscreen = true;
		      player.classList.add('c-player');

		      return player;
		    } }, { key: 'renderControls', value: function renderControls()

		    {var _this = this;
		      var controls = document.createElement('div');
		      controls.classList.add('c-controls');

		      var playElem = this.createActionHandler({
		        inputType: 'button',
		        labelText: '再生',
		        className: 'playButton',
		        onClick: function onClick() {return _this.postMessage({
		            eventName: _this.state.playerStatus === 2 ? 'pause' : 'play' });} });
		      var repeatElem = this.createActionHandler({
		        inputType: 'checkbox',
		        labelText: 'リピート',
		        className: 'repeatToggle',
		        onChange: function onChange(e) {
		          _this.state = Object.assign({}, _this.state, {
		            isRepeat: e.target.checked });

		        } });
		      var muteElem = this.createActionHandler({
		        inputType: 'checkbox',
		        labelText: 'ミュート',
		        className: 'muteToggle',
		        attributes: {
		          disabled: true },

		        onChange: function onChange(e) {return _this.postMessage({
		            eventName: 'mute',
		            data: {
		              mute: e.target.checked } });} });




		      var seekElem = this.createActionHandler({
		        inputType: 'range',
		        labelText: 'シーク',
		        className: 'seekRange',
		        attributes: {
		          value: 0,
		          disabled: true },

		        onChange: function onChange(e) {return _this.postMessage({
		            eventName: 'seek',
		            data: {
		              time: e.target.valueAsNumber } });} });




		      var volumeElem = this.createActionHandler({
		        inputType: 'range',
		        labelText: '音量',
		        className: 'volumeRange',
		        attributes: {
		          value: 0,
		          max: 1,
		          step: 0.01,
		          disabled: true },

		        onChange: function onChange(e) {return _this.postMessage({
		            eventName: 'volumeChange',
		            data: {
		              volume: e.target.valueAsNumber } });} });




		      controls.appendChild(playElem);
		      controls.appendChild(muteElem);
		      controls.appendChild(repeatElem);
		      controls.appendChild(seekElem);
		      controls.appendChild(volumeElem);

		      return controls;
		    } }, { key: 'createActionHandler', value: function createActionHandler(options) {
		      var label = document.createElement('label');
		      var input = document.createElement('input');

		      input.classList.add('c-' + options.className);
		      input.type = options.inputType;

		      if (options.hasOwnProperty('attributes')) {
		        Object.entries(options.attributes).forEach(function (_ref5) {
		        	var _ref6 = _slicedToArray(_ref5, 2),key = _ref6[0],value = _ref6[1];
		        	return input[key] = value;
		        });
		      }
		      if (options.hasOwnProperty('onClick')) {
		        input.onclick = options.onClick;
		      }
		      if (options.hasOwnProperty('onChange')) {
		        input.onchange = options.onChange;
		      }
		      if (options.hasOwnProperty('labelText')) {
		        switch (options.inputType) {
		          case 'button':
		            input.value = options.labelText;
		            break;

		          default:{
		              var span = document.createElement('span');
		              span.innerText = options.labelText;
		              label.appendChild(span);
		            }}

		      }

		      label.appendChild(input);

		      return label;
		    } }, { key: 'messageListener', value: function messageListener()

		    {var _this2 = this;
		      window.addEventListener('message', function (e) {
		        if (e.origin === NicovideoPlayer.origin && e.data.playerId === _this2.playerId) {var
		          data = e.data.data;

		          switch (e.data.eventName) {
		            case 'playerMetadataChange':{
		                _this2.playerMetadataChange(data);
		                break;
		              }

		            case 'playerStatusChange':{
		                _this2.playerStatusChange(data);
		                break;
		              }

		            case 'loadComplete':{
		                _this2.renderInfoTable(data.videoInfo);
		                break;
		              }

		            default:
		              console.log(e.data);}


		          _this2.state = Object.assign({}, _this2.state, data);
		        }
		      });
		    } }, { key: 'playerMetadataChange', value: function playerMetadataChange(

		    data) {
		      var seek = this.controls.querySelector('.c-seekRange');
		      var mute = this.controls.querySelector('.c-muteToggle');
		      var volume = this.controls.querySelector('.c-volumeRange');

		      mute.disabled = false;
		      volume.disabled = false;

		      // 動画の長さを更新
		      if (data.duration !== undefined && data.duration !== seek.max) {
		        seek.max = data.duration;
		        seek.disabled = false;
		      }

		      // 再生時間の更新
		      if (data.currentTime !== undefined && data.currentTime !== seek.time) {
		        seek.value = data.currentTime;
		        this.seekProgress(seek, data);
		      }

		      // ミュートの更新
		      if (data.muted !== mute.checked) {
		        mute.checked = data.muted;
		      }

		      // 音量の更新
		      if (data.volume !== volume.valueAsNumber) {
		        volume.value = data.volume;
		      }

		      // バッファの更新
		      if (this.state.maximumBuffered !== data.maximumBuffered) {
		        this.seekProgress(seek, data);
		      }
		    } }, { key: 'playerStatusChange', value: function playerStatusChange(

		    data) {
		      switch (data.playerStatus) {
		        case 1: // 読み込み開始
		        case 2: // 再生開始
		          this.playButtonChange(false);
		          break;

		        case 3: // 一時停止
		        case 4: // 再生終了
		          if (data.playerStatus === 4 && this.state.isRepeat) {
		            this.postMessage({
		              eventName: 'seek',
		              data: {
		                time: 0 } });


		          } else {
		            this.playButtonChange(true);
		          }
		          break;}

		    } }, { key: 'playButtonChange', value: function playButtonChange(

		    isPaused) {
		      var playButton = this.controls.querySelector('.c-playButton');

		      if (isPaused) {
		        playButton.value = '再生';
		      } else {
		        playButton.value = '停止';
		      }
		    } }, { key: 'seekProgress', value: function seekProgress(

		    seek, data) {
		      var timeRate = Math.floor(1000 / (data.duration / data.currentTime)) / 10;
		      var bufferRate = Math.floor(1000 / (data.duration / data.maximumBuffered)) / 10;

		      seek.style.background = 'linear-gradient(to right, #38f ' + timeRate + '%, #ccc ' + timeRate + '%, #ccc ' + bufferRate + '%,  #555 ' + bufferRate + '%)';
		    } }, { key: 'postMessage', value: function postMessage(request) {
		      var message = Object.assign({
		        sourceConnectorType: 1,
		        playerId: this.playerId },
		      request);
		      this.player.contentWindow.postMessage(message, NicovideoPlayer.origin);
		    } }]);return NicovideoPlayer;}();NicovideoPlayer.playerId = 0;NicovideoPlayer.origin = 'https://embed.nicovideo.jp';
