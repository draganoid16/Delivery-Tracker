from flask import Flask, jsonify, request

app = Flask(__name__)

tracking_data = {
    "1": {
        "trackingNumber": "test",
        "carrier-slug": "CTT",
        "status": "Delivered",
        "estimated_date_delivery": "Estimated Delivery: October 13, 2015",
        "checkpoints": [
            {
                "message": "Package received",
                "checkpointTime": "2023-04-15T10:30:00",
                "city": "Lisbon",
                "country": "Portugal",
                "location": "Lisbon Sorting Center"
            },
            {
                "message": "Package in transit",
                "checkpointTime": "2023-04-16T14:00:00",
                "city": "Porto",
                "country": "Portugal",
                "location": "Porto Distribution Center"
            },
            {
                "message": "Delivered",
                "checkpointTime": "2023-04-16T14:00:00",
                "city": "Porto",
                "country": "Portugal",
                "location": "Porto Distribution Center"
            }
        ]
    },
    "2": {
        "trackingNumber": "222333444",
        "carrier-slug": "UPS",
        "status": "in_transit",
        "estimated_date_delivery": "Estimated Delivery: October 13, 2015",
        "checkpoints": [
            {
                "message": "Package received",
                "checkpointTime": "2023-04-15T10:30:00",
                "city": "Lisbon",
                "country": "Portugal",
                "location": "Lisbon Sorting Center"
            },
            {
                "message": "Package in transit",
                "checkpointTime": "2023-04-16T14:00:00",
                "city": "Porto",
                "country": "Portugal",
                "location": "Porto Distribution Center"
            }
        ]
    }
}

@app.route('/tracking', methods=['GET'])
def get_tracking_info():
    carrier_slug = request.args.get('carrier_slug')
    tracking_number = request.args.get('tracking_number')

    for data in tracking_data.values():
        if data['carrier-slug'] == carrier_slug and data['trackingNumber'] == tracking_number:
            return jsonify(data)

    return jsonify({"error": "Tracking not found!"}), 404

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
