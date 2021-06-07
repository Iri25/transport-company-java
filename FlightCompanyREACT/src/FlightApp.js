import {AddFlight, DeleteFlight, GetFlights} from "./utils/rest-calls";
import FlightForm from "./FlightForm";
import FlightTable from "./Flight";
import React from  'react';
import "./FlightApp.css";

class FlightApp extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            flights:[{"id":"default","destination":"default","departureDate":"default","departureTime":"default","airport":"default","numberOfSeats":"default"}],
            deleteFunc:this.deleteFunc.bind(this),
            addFunc:this.addFunc.bind(this),
        }
        console.log('FlightApp constructor')
    }

    addFunc(flight){
        console.log('inside add Func ' + flight);
        AddFlight(flight)
            .then(res=> GetFlights())
            .then(flights=>this.setState({flights}))
            .catch(error=>console.log('error add ',error));
    }

    deleteFunc(flight){
        console.log('inside deleteFunc ' + flight);
        DeleteFlight(flight)
            .then(res=> GetFlights())
            .then(flights=>this.setState({flights}))
            .catch(error=>console.log('error delete', error));
    }

    componentDidMount(){
        console.log('inside componentDidMount')
        GetFlights().then(flights=>this.setState({flights}));
    }

    render(){
        return(
            <div className="FlightApp">
                <h1>Flight Company</h1>
                <FlightForm addFunc={this.state.addFunc}/>
                <br/>
                <br/>
                <FlightTable flights={this.state.flights} deleteFunc={this.state.deleteFunc}/>
            </div>
        )
    }
}


export default FlightApp;