import React from 'react';
import {
  View,
  ListView,
  StyleSheet,
  Navigator,
  TouchableOpacity,
  Text
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
      ds: [{name: "Straja", visited: false, country: "Romania"},{name: "Solden", visited: true, country: "Austria"}],
      dataSource:ds
    };

  }

  componentDidMount(){
    this.setState({
      dataSource:this.state.dataSource.cloneWithRows(this.state.ds),
    })

  }

  render(){
    return (
      	<ListView style={styles.container}
          enableEmptySections={true}
          dataSource={this.state.dataSource}
          renderRow={(rowData) =>
            <TouchableOpacity onPress={()=> this.props.navigator.push({index: 1,
               passProps:{
                   name: rowData.name,
                   visited: rowData.visited,
                   country: rowData.country
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
