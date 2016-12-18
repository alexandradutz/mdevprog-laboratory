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
  Alert
} from 'react-native';

import * as Progress from 'react-native-progress';

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

class ListScreen extends React.Component {
  constructor(props){
    super(props);

    const ds = new ListView.DataSource({
        rowHasChanged: (r1, r2) => r1 !== r2
    });

    this.state = {
        dataSource: ds.cloneWithRows(['row 1', 'row 2']),
        resorts:[],
        loaded:false,
        tempResort: {name:'',visited:false,country:''},
        toBeDeleted: 0
    };
  }

  componentDidMount(){
      this.fetchData().done();
      this.addResort = this.addResort.bind(this);
      this.del = this.del.bind(this);
      this.updateResort = this.updateResort.bind(this);
};

fetchData=async()=>{
    // console.error("THIS");

    const value = await AsyncStorage.getItem('@Resorts:storage');
    try {
        if(value) {
            this.state.resorts = JSON.parse(value);
        }
        else {
            this.setState({tempResort:{name:"name", visited:false, country:"country"}, toBeDeleted:0});
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
    x.splice(this.state.toBeDeleted, 1);
    AsyncStorage.setItem('@Resorts:storage', JSON.stringify(x));
    this.setState({
        dataSource:this.state.dataSource.cloneWithRows(x),
        resorts:x
    });
}

delAlert(){
    Alert.alert( 'Delete Action', 'Are you sure you want to delete this?',
        [ {text: 'Cancel', onPress: () => console.log('Cancel Pressed'), style: 'cancel'},
          {text: 'OK', onPress: () => {this.del}}, ] );
}

addResort(){
     var x = this.state.resorts.slice();
     if(this.state.tempResort.name != "") {
         var res1 = {id:x.length, name:this.state.tempResort.name, visited:this.state.tempResort.visited, country:this.state.tempResort.country};
         x.push(res1);
     }
     AsyncStorage.setItem('@Resorts:storage', JSON.stringify(x));
      this.setState({
             dataSource: this.state.dataSource.cloneWithRows(x),
             resorts:x,
    });
 }

 updateResort(id, n, v, c) {
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
     }
     AsyncStorage.setItem('@Resorts:storage', JSON.stringify(x));
     this.setState({
         dataSource: this.state.dataSource.cloneWithRows(x),
         resorts: x,
    });

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
                   addFunction: this.addResort,
                   delFunction: this.del,
                   updateResort: this.updateResort
               }})}
               onLongPress={() => {this.setState({toBeDeleted:rowData.id}); this.delAlert()}}>
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
