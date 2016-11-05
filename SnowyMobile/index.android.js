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

const routes = [
  {
    title: 'Snowy',
    index: 0
  }, {
    title: 'Details',
    index: 1
  }
]

class SnowyMobile extends Component {
  state = {
  }

  render() {
    return (
          <View style={styles.container}>
            <StatusBar
              backgroundColor="lightblue"
              barStyle="light-content"
            />
            <Navigator
              initialRoute={routes[0]}
              initialRouteStack={routes}
              renderScene={
                (route, navigator) => {
                  switch (route.index) {
                    case 0: return (<ListScreen navigator={navigator} route={routes[route.index]} {...route.passProps}></ListScreen>);
                    case 1: return (<DetailScreen navigator={navigator} route={routes[route.index]} {...route.passProps}></DetailScreen>);
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
