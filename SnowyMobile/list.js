import React from 'react';
import {
  View,
  ListView,
  StyleSheet,
  Navigator,
  TouchableOpacity,
  Text,
  AsyncStorage
} from 'react-native';

import * as Progress from 'react-native-progress';

import InfiniteScrollView from 'react-native-infinite-scroll-view';

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
        loaded:false
    };
  }

  componentDidMount(){
      console.log(this);
      this.fetchData().done();
      this.addResort = this.addResort.bind(this);
      this.del = this.del.bind(this);
};

fetchData=async()=>{
    console.log("THIS");
    console.log(this);
    const value = await AsyncStorage.getItem('@Resorts:what');
    try {
        this.state.resorts = JSON.parse(value);
    } catch(error) {
        console.log(error);
    }
    finally{
        if(this.state.resorts.length<1){
            var res1={id:0,name:"name1",visited:"true",country:"country1"};
            this.state.resorts.push(res1);
        }
        }

this.setState({
  dataSource:this.state.dataSource.cloneWithRows(this.state.resorts),
  loaded:true
});

} //fetchData

del(it){
    var x = this.state.resorts.slice();
    x.splice(it, 1);
    AsyncStorage.setItem('@Resorts:what', JSON.stringify(x));
    this.setState({
        dataSource:this.state.dataSource.cloneWithRows(x),
        resorts:x
    });
}

addResort(it,n,v,c){

     var edited = false;
     var x = this.state.resorts.slice();
     for(var i=0;i<x.length;i++){
       if(x[i].id==it){
         var res1={id:it,name:n,visited:v,country:c};
         x[i] = res1;
         edited=true;
       }
     }
     if(!edited){
       var res1={id:x.length,name:n,visited:v,country:c};
       x.push(res1);
     }
      AsyncStorage.setItem('@Resorts:what', JSON.stringify(x));
     //  console.log("xyz");
     //  console.warn(JSON.stringify(x));
      this.setState({
             dataSource: this.state.dataSource.cloneWithRows(x),
             resorts:x,
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
                   delFunction: this.del
                }})}>
               <View>
                 <Text style={styles.symbol}>{rowData.name}</Text>
               </View>
            </TouchableOpacity>
	  }
          renderSeparator={(sectionID, rowID, adjacentRowHighlighted) =>
            <View key={rowID} style={{height:1, backgroundColor: 'lightgray'}}/>
          }
       	/>
    );
  }
}

export default ListScreen;
