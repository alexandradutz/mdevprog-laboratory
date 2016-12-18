import React, {Component} from 'react';
import {
   TouchableHighlight,
   Image,
   AppRegistry,
   StyleSheet,
   Text,
   TextInput,
   View,
   Switch,
   Linking
} from 'react-native';
import Chart from 'react-native-chart';
class DetailScreen extends Component {
  constructor(props){
    super(props);
    this.state = {
      id:0,
      name:"name",
      visited:false,
      country:"country",
      words:[["november",1],["december",20],["january",10],["february",15]],
      url: 'mailto:?subject='+this.props.name+"&body="+this.props.country+"\n\n\n\n================\nBackup from your Snowy account"
    };
  }

  startIntent = () => {
      var url = this.state.url;
      Linking.canOpenURL(url).then(supported => { if (!supported) { console.log('Can\'t handle url: ' + url); }
        else { return Linking.openURL(url); } }).catch(err => console.error('An error occurred', err));
  };

  componentDidMount(){
    this.setState(
      {
        id:this.props.id,
        name:this.props.name,
        visited:this.props.visited,
        country:this.props.country
      }
    )
  }

  render() {
    return (
      <View style={styles.container}>
	     <TextInput placeholder={this.props.name} onChangeText={(text) => this.setState({name:text}) } />
       <Text style={{paddingTop: 10}}>Visited</Text><Switch onValueChange={(value)=> this.setState({visited:value})}
                value={this.state.visited} />
        <TextInput placeholder={this.props.country} onChangeText={(text) => this.setState({country:text}) } />

        <TouchableHighlight onPress={()=>{this.props.updateResort(
                                    this.state.id,
                                    this.state.name,
                                    this.state.visited,
                                    this.state.country); this.props.navigator.pop();}}>

            <Text style={{ color: 'black', padding: 10, fontSize: 15}}>Submit</Text>
        </TouchableHighlight>
        <TouchableHighlight onPress={this.startIntent}>
            <Text style={{ color: 'black', padding: 10, fontSize: 15}}>Send mail</Text>
        </TouchableHighlight>
        <Chart
                  style={styles.chart}
                  data={this.state.words}
                  verticalGridStep={5}
                  type="line"
                  showDataPoint={true}
                  color={"red"}
               />
      </View>
    );
  }
}

var styles = StyleSheet.create({
  container:{
    flex:1,
    padding: 10,
    paddingTop:70,
  },
  chart: {
       width: 400,
       height: 200,
   },
});

export default DetailScreen;
