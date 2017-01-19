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
import DatePicker from 'react-native-datepicker'
class DetailScreen extends Component {
  constructor(props){
    super(props);
    this.state = {
      id:0,
      name:"name",
      visitedOn: "2016-02-12",
      visited:false,
      country:"country",
      words:[["november",1],["december",20],["january",10],["february",15]],
      _key: "",
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
        country:this.props.country,
        _key: this.props._key
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
                                    this.state.country,
                                    this.state.visitedOn,
                                    this.state._key); this.props.navigator.pop();}}>

            <Text style={{ color: 'black', padding: 10, fontSize: 15}}>Submit</Text>
        </TouchableHighlight>
        <TouchableHighlight onPress={this.startIntent}>
            <Text style={{ color: 'black', padding: 10, fontSize: 15}}>Send mail</Text>
        </TouchableHighlight>

        <Text  style={{paddingTop: 10}}> Visited on </Text>
        <DatePicker
          style={{width: 200, paddingBottom: 20}}
          date={this.state.visitedOn}
          mode="date"
          placeholder="select date"
          format="YYYY-MM-DD"
          minDate="2016-05-01"
          maxDate="2017-02-02"
          confirmBtnText="Confirm"
          cancelBtnText="Cancel"
          onDateChange={(date) => {this.setState({visitedOn: date})}}
        />

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
       width: 300,
       height: 200,
   },
});

export default DetailScreen;
