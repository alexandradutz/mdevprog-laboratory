import React from 'react';
import {
  View,
  ListView,
  StyleSheet,
  Navigator,
  TouchableOpacity,
  Text,
  AsyncStorage,
  TouchableHighlight,
  TextInput,
  Switch,
  Alert,
  AppState
} from 'react-native';
import * as firebase from 'firebase';
import * as Progress from 'react-native-progress';
import PushNotification from 'react-native-push-notification';

import InfiniteScrollView from 'react-native-infinite-scroll-view';
import update from 'immutability-helper';

var DomParser = require('xmldom').DOMParser;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginTop: 102,
  },
  separator: {
    flex: 1,
    height: StyleSheet.hairlineWidth,
    backgroundColor: '#8E8E8E',
  },
  progress: {
    marginTop: 80,
  },
});

// Initialize Firebase
const config = {
  apiKey: "AIzaSyCYRoYXnkhG4lxizDctX87yvS2MHMWbTiE",
  authDomain: "snowyreact.firebaseapp.com",
  databaseURL: "https://snowyreact.firebaseio.com",
  storageBucket: "snowyreact.appspot.com",
  messagingSenderId: "97865845381"
};
const firebaseApp = firebase.initializeApp(config);


class ListScreen extends React.Component {
  constructor(props){
    super(props);

   this.resortsRef = firebaseApp.database().ref();
    const ds = new ListView.DataSource({
        rowHasChanged: (r1, r2) => r1 !== r2
    });

    this.state = {
        dataSource: ds.cloneWithRows(['row 1', 'row 2']),
        resorts:[],
        loaded:false,
        tempResort: {name:'',visited:false,country:'', visitDate:"2000-01-01"},
        toBeDeleted: 0,
        toBeDelKey: "",
        resortsRef: this.resortsRef,
        currentAppState: AppState.currentState
    };
  }

  syncResorts(resortsRef) {
      resortsRef.on('value', (snap) => {

          // get children as an array
            var items = [];
            snap.forEach((child) => {
              items.push({
                id: child.val().id,
                name: child.val().name,
                visited: child.val().visited,
                country: child.val().country,
                _key: child.key,
                currentAppState: AppState.currentState
              });
            });

            this.setState({
              dataSource: this.state.dataSource.cloneWithRows(items),
              loaded: true
            });

          });
  }

  componentDidMount(){
    //   this.fetchData().done();
      this.syncResorts(this.state.resortsRef);
      this.addResort = this.addResort.bind(this);
      this.del = this.del.bind(this);
      this.updateResort = this.updateResort.bind(this);

      this.state.resortsRef.on('child_added', snap => {
        this.addResortSnapshot(snap);
        this.updateListViewDataSource(this.state.resorts);
        if(this.state.currentAppState === 'background') {
        PushNotification.localNotificationSchedule({
                    message: "Notification++",
                    date: new Date(Date.now())
                });
        }
      });

//      this.state.resortsRef.on('child_added', snap => {
//              this.addResortSnapshot(snapshot);
//              //update list
//              if(this.state.currentAppState === 'background') {
//              PushNotification.localNotificationSchedule({
//                          message: "Notification++",
//                          date: new Date(Date.now())
//                      });
//              }
//            });
};

 updateListViewDataSource(data) {
        const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
        this.setState({
            dataSource: ds.cloneWithRows(data)
        });
}

 addResortSnapshot(snapshot) {
        let resort = snapshot.val();
        resort._key = snapshot.key;
        this.state.resorts.push(resort);
}

componentWillUpdate(){
    this.fetchData().done();
};

fetchData=async()=>{
    const value = await AsyncStorage.getItem('@Resorts:storage');
    try {
        if(value) {
            this.state.resorts = JSON.parse(value);
        }
        else {
            this.setState({tempResort:{name:"name", visited:false, country:"country", visitDate:"2000-01-01"}, toBeDeleted:0, _key:""});
            this.addResort();
        }

    } catch(error) {
        console.log(error);
    }

this.setState({
  dataSource:this.state.dataSource.cloneWithRows(this.state.resorts),
  loaded:true
});

} //fetchData

del(){
    var x = this.state.resorts.slice();
    var i = 0;
    while(i < x.length)
    {
        if(this.state.resorts[i].id == this.state.toBeDeleted)
        {
            break;
        }
        else {
            i = i+1;
        }
    }
    this.state.resortsRef.child(this.state.toBeDelKey).remove();
    x.splice(i, 1);
    var newSet = x.slice();
    AsyncStorage.setItem('@Resorts:storage', JSON.stringify(newSet));
    this.setState({
        dataSource:this.state.dataSource.cloneWithRows(newSet),
        resorts:newSet,
    });
}

delAlert(_key){
    Alert.alert( 'Delete Action', 'Are you sure you want to delete this?',
        [ {text: 'Cancel', onPress: () => console.log('Cancel Pressed'), style: 'cancel'},
          {text: 'OK', onPress: () => this.del()}, ] );
}

addResort(){
     var x = this.state.resorts.slice();
     if(this.state.tempResort.name != "") {
         var res1 = {id:x.length, name:this.state.tempResort.name, visited:this.state.tempResort.visited, country:this.state.tempResort.country,
                        visitDate:"2000-01-01", _key:""};
         x.push(res1);
         this.state.resortsRef.push(res1);
     }
     AsyncStorage.setItem('@Resorts:storage', JSON.stringify(x));
      this.setState({
             dataSource: this.state.dataSource.cloneWithRows(x),
             resorts:x,
    });
 }

 updateResort(id, n, v, c, date, key) {
     var x = this.state.resorts.slice();
     var i = 0;
     while(i < x.length)
     {
         if(this.state.resorts[i].id == id)
         {
             break;
         }
         else {
             i = i+1;
         }
     }
     if(i < x.length)
     {
         x[i].name = n;
         x[i].country = c;
         x[i].visited = v;
         x[i].visitDate = date;
     }
     var newSet = x.slice();
     AsyncStorage.setItem('@Resorts:storage', JSON.stringify(newSet));
    //  console.error(newSet);
     this.setState({
         dataSource: this.state.dataSource.cloneWithRows(newSet),
         resorts: newSet,
    });
     this.state.resortsRef.child(key).update();

 }

  render(){
      if (!this.state.loaded){
      return (<View style={styles.progress}>
             <Text>Please wait ... </Text>
             <Progress.Bar progress={0.3} width={200} indeterminate={true} />
          </View>
     );
    }
    return (
        <View style={styles.container}>
        <View>
        <TextInput placeholder={'Name'} onChangeText={(text) => this.setState({tempResort:{
                                                                                            name:text,
                                                                                            visited:this.state.tempResort.visited,
                                                                                            country:this.state.tempResort.country}}) } />
        <Text style={{paddingTop: 10}}>Visited</Text><Switch onValueChange={(value)=> this.setState({tempResort:{
                                                                                            name:this.state.tempResort.name,
                                                                                            visited:value,
                                                                                            country:this.state.tempResort.country}})}
                                                    value={this.state.tempResort.visited} />
        <TextInput placeholder={'Country'} onChangeText={(text) => this.setState({tempResort:{
                                                                                            name:this.state.tempResort.name,
                                                                                            visited:this.state.tempResort.visited,
                                                                                            country:text}}) } />
        </View>

        <TouchableHighlight onPress={this.addResort}>
            <Text style={{ color: 'black', padding: 10, fontSize: 15}}>Add Resort</Text>
        </TouchableHighlight>


      	<ListView style={styles.container}
          enableEmptySections={true}
          dataSource={this.state.dataSource}
          renderRow={(rowData) =>
            <TouchableOpacity onPress={()=> this.props.navigator.push({index: 1,
               passProps:{
                   id: rowData.id,
                   name: rowData.name,
                   visited: rowData.visited,
                   country: rowData.country,
                   visitedOn: rowData.visitedOn,
                   _key: rowData._key,
                   addFunction: this.addResort,
                   delFunction: this.del,
                   updateResort: this.updateResort
               }})}
               onLongPress={() => {this.setState({toBeDelKey:rowData._key, toBeDeleted:rowData.id}); this.delAlert()}}>
               <View>
                 <Text style={styles.symbol}>{rowData.name}</Text>
               </View>
            </TouchableOpacity>
	  }
          renderSeparator={(sectionID, rowID, adjacentRowHighlighted) =>
            <View key={rowID} style={{height:1, backgroundColor: 'lightgray'}}/>
          }
       	/>
        </View>
    );
  }
}

export default ListScreen;
