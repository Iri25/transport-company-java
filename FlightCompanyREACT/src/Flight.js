
import React from  'react';

class FlightRow extends React.Component{

    handleClicked=(event)=>{
        console.log('delete button for '+ this.props.flight.id);
        this.props.deleteFunc(this.props.flight.id);
    }

    render() {
        return (
            <tr>
                <td>{this.props.flight.id}</td>
                <td>{this.props.flight.destination}</td>
                <td>{this.props.flight.departureDate}</td>
                <td>{this.props.flight.departureTime}</td>
                <td>{this.props.flight.airport}</td>
                <td>{this.props.flight.numberOfSeats}</td>
                <td><button  onClick={this.handleClicked}>Delete</button></td>
            </tr>
        );
    }
}

class FlightTable extends React.Component{
    render(){
        var rows = [];
        var functionStergere= this.props.deleteFunc;

        this.props.flights.forEach(function(flight) {
            rows.push(<FlightRow flight={flight} key={flight.id} deleteFunc={functionStergere} />);
        });
        return (<div className="FlightTable">
                <table className="center">
                    <thead>
                    <tr>
                        <th>Id</th>
                        <th>Destination</th>
                        <th>Departure Date</th>
                        <th>Departure Time</th>
                        <th>Airport</th>
                        <th>Number Of Seats</th>

                        <th></th>
                    </tr>
                    </thead>
                    <tbody>{rows}</tbody>
                </table>

            </div>
        );
    }
}

export default FlightTable;