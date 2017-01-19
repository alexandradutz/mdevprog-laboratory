import React, { PropTypes, Component } from 'react';
var {FBLogin, FBLoginManager} = require('react-native-facebook-login');
var {FBLoginManager} = require('react-native-facebook-login');

import {
   Navigator,
   StatusBar,
   TouchableHighlight,
   AppRegistry,
   StyleSheet,
   Text,
   View
} from 'react-native';
class Login extends React.Component {
  render() {
    var _this = this;
  return (
    <View>
    <Text style = {{marginTop:150}}>
    Login with facebook
    </Text>
    <FBLogin style={{ marginBottom: 10}}
      ref={(fbLogin) => { this.fbLogin = fbLogin }}
      permissions={["email","user_friends"]}
      loginBehavior={FBLoginManager.LoginBehaviors.Native}
      onLogin={function(data){
        console.log("Logged in!");
        console.log(data);
        _this.setState({ user : data.credentials });
      }}
      onLogout={function(){
        console.log("Logged out.");
        _this.setState({ user : null });
      }}
      onLoginFound={function(data){
        console.log("Existing login found.");
        console.log(data);
        _this.setState({ user : data.credentials });
      }}
      onLoginNotFound={function(){
        console.log("No user logged in.");
        _this.setState({ user : null });
      }}
      onError={function(data){
        console.log("ERROR");
        console.log(data);
      }}
      onCancel={function(){
        console.log("User cancelled.");
      }}
      onPermissionsMissing={function(data){
        console.log("Check permissions!");
        console.log(data);
      }}
    />
    </View>
  );

  }
};
export default Login;
