import React from 'react';

class FlightForm extends React.Component{

    constructor(props) {
        super(props);
        this.state = {username: '', name:'', passwd:''};
    }

   handleIdChange=(event) =>{
           this.setState({id: event.target.value});
       }

       handleDestinationChange=(event) =>{
           this.setState({destination: event.target.value});
       }

       handleDepartureDateChange=(event) =>{
           this.setState({departureDate: event.target.value});
       }

       handleDepartureTimeChange=(event) =>{
           this.setState({departureTime: event.target.value});
       }

       handleAirportChange=(event) =>{
           this.setState({airport: event.target.value});
       }

       handleNumberOfSeatsChange=(event) =>{
           this.setState({numberOfSeats: event.target.value});
       }

    handleSubmit =(event) =>{

        var flight = {
            id:this.state.id,
            destination:this.state.destination,
            departureDate:this.state.departureDate,
            departureTime:this.state.departureTime,
            airport:this.state.airport,
            numberOfSeats:this.state.numberOfSeats
        }
        console.log('A Flight was submitted: ');
        console.log(flight);
        this.props.addFunc(flight);
        event.preventDefault();
    }


    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>
                    Id:
                    <input type="text" value={this.state.id} onChange={this.handleIdChange} />
                </label><br/>
                <label>
                    Destination:
                    <input type="text" value={this.state.destination} onChange={this.handleDestinationChange} />
                </label><br/>
                <label>
                    Departure Date:
                    <input type="text" value={this.state.departureDate} onChange={this.handleDepartureDateChange} />
                </label><br/>
                <label>
                    Departure Time:
                    <input type="text" value={this.state.departureTime} onChange={this.handleDepartureTimeChange} />
                </label><br/>
                <label>
                    Airport:
                    <input type="text" value={this.state.airport} onChange={this.handleAirportChange} />
                </label><br/>
                <label>
                    Number Of Seats:
                    <input type="text" value={this.state.numberOfSeats} onChange={this.handleNumberOfSeatsChange} />
                </label><br/>

                <input type="submit" value="Submit" />
            </form>
        );
    }
}

export default FlightForm;