/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Navigator,
  StatusBar,
  TouchableHighlight,
  AppRegistry,
  StyleSheet,
  Text,
  View,
  Linking
} from 'react-native';


import ListScreen from './list.js';
import DetailScreen from './details.js';
import MyDatePicker from './datepicker.js'
import Login from "./login.js";
const routes = [
  {
    title: 'Snowy',
    index: 0
  }, {
    title: 'Details',
    index: 1
}, {
    title: 'Picker',
    index: 2
}, {
    title: 'Login',
    index: 3
}
]
export default class SnowyMobile extends Component {


  render() {
    return (
          <View style={styles.container}>
            <StatusBar
              backgroundColor="lightblue"
              barStyle="light-content"
            />
            <Navigator
              initialRoute={routes[3]}
              initialRouteStack={routes}
              renderScene={
                (route, navigator) => {
                  switch (route.index) {
                    case 0: return (<ListScreen ref="listScreen" navigator={navigator} route={routes[route.index]} {...route.passProps}></ListScreen>);
                    case 1: return (<DetailScreen navigator={navigator} route={routes[route.index]} {...route.passProps}></DetailScreen>);
                    case 2: return (<MyDatePicker navigator={navigator} route={routes[routes.index]} {...route.passProps}></MyDatePicker>);
                    case 3: return (<Login/>);
                  }
                }
              }
              configureScene={
                (route, routeStack) =>
                  Navigator.SceneConfigs.FloatFromBottom
              }
              navigationBar={
               <Navigator.NavigationBar
                 routeMapper={{
                   LeftButton: (route, navigator, index, navState) => {
                     if (route.index == 0){
                       return null;
                     }
                     else if(route.index == 3)
                     {
                         return (
                         <TouchableHighlight onPress={()=>{navigator.pop();navigator.pop();navigator.pop();}}>
                           <Text style={styles.navigationBarText}>Back</Text>
                         </TouchableHighlight>
                     )
                     }
                     return (
                       <TouchableHighlight onPress={()=>navigator.pop()}>
                         <Text style={styles.navigationBarText}>Back</Text>
                       </TouchableHighlight>
                     )
                   },
                   RightButton: (route, navigator, index, navState) => { return null; },
                   Title: (route, navigator, index, navState) =>
                     { return (<Text style={[styles.navigationBarText, styles.titleText]}>{routes[route.index].title}</Text>); },
                 }}
                 style={styles.navigationBar}
               />
            }
          />
    </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  navigationBar:{
    backgroundColor: 'lightblue',
  },
  navigationBarText:{
    color: 'darkblue',
    padding: 10,
    fontSize: 15
  },
  titleText:{
    fontSize: 20,
    paddingTop:5
  }

});


AppRegistry.registerComponent('SnowyMobile', () => SnowyMobile);
