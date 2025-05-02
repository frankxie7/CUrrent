from db import db, Rental, User, Listing
from flask import Flask, request
from datetime import datetime,timedelta
import json

app = Flask(__name__)

db_filename = "current.db"

app.config["SQLALCHEMY_DATABASE_URI"] = f"sqlite:///{db_filename}"
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
app.config["SQLALCHEMY_ECHO"] = True

db.init_app(app)
with app.app_context():
    db.create_all()

def success_response(data, code=200):
    return json.dumps(data), code

def failure_response(message, code=404):
    return json.dumps({"error": message}), code

#Handles user methods
@app.route("/api/users/", methods=["GET", "POST"])
def handle_users():
    if request.method == "GET":
        users = [u.serialize_no_relations() for u in User.query.all()]
        return success_response({"courses": users})
    else: 
        try:
            body = json.loads(request.data)
            new_user = User(
            name=body.get("name"),
            email=body.get("email")
            )
            db.session.add(new_user)
            db.session.commit()
            return json.dumps(new_user.serialize()), 201
        except:
            return failure_response("Failed to create user", 400)
        
#Handles specific user methods
@app.route("/api/users/<int:user_id>",methods=["GET", "DELETE"])
def handle_specific_user(user_id):
    if request.method == "GET":
        user = User.query.filter_by(id=user_id).first()
        if user is None:
            return failure_response("User not found")
        return success_response(user.serialize())
    elif request.method == "DELETE":
        user = User.query.filter_by(id=user_id).first()
        if user is None:
            return failure_response("User not found")
        db.session.delete(user)
        db.session.commit()
        return json.dumps(user.serialize()),200
    else:
        return failure_response("Method not supported", 400)

@app.route("/api/listings/" , methods = ["GET", "POST"])
def handle_listing():
    if request.method == "GET":
        listings = [l.serialize_no_user() for l in Listing.query.all()]
        return success_response({"listings": listings})
    elif request.method == "POST":
        body = json.loads(request.data)
        required_fields = ['vehicle_type', 'price_per_day', 'available_from', 'available_to', 'owner_id']
        if not all(field in body for field in required_fields):
            return failure_response('Missing required fields', 400)
        new_listing = Listing(
            vehicle_type = body["vehicle_type"],
            price_per_day = float(body["price_per_day"]),
            available_from=datetime.strptime(body['available_from'], '%Y-%m-%d').date(),
            available_to=datetime.strptime(body['available_to'], '%Y-%m-%d').date(),
            original_available_from= datetime.strptime(body['available_from'], '%Y-%m-%d').date(),
            original_available_to= datetime.strptime(body['available_to'], '%Y-%m-%d').date(),
            owner_id = int(body["owner_id"]),
            is_rented= body.get('is_rented', False)
        )
        db.session.add(new_listing)
        db.session.commit()
        return success_response(new_listing.serialize())

@app.route("/api/listings/<int:listing_id>/" , methods = ["GET", "DELETE","PUT"])
def handle_specific_listing(listing_id):
    if request.method == "GET":
        listing = Listing.query.filter_by(id=listing_id).first()
        if listing is None:
            return failure_response("Listing not found")
        return success_response(listing.serialize())
    elif request.method == "DELETE":
        listing = Listing.query.filter_by(id=listing_id).first()
        if listing is None:
            return failure_response("Listing not found")
        db.session.delete(listing)
        db.session.commit()
        return success_response(listing.serialize())
    elif request.method == "PUT":
        listing = Listing.query.filter_by(id=listing_id).first()
        data = json.load(request.data)
        if listing is None:
            return failure_response("Listing not found")
        if 'vehicle_type' in data:
            listing.vehicle_type = data['vehicle_type']
        if 'price_per_day' in data:
            listing.price_per_day = float(data['price_per_day'])
        if 'available_from' in data:
            listing.available_from = datetime.strptime(data['available_from'], '%Y-%m-%d').date()
        if 'available_to' in data:
            listing.available_to = datetime.strptime(data['available_to'], '%Y-%m-%d').date()
        if 'is_rented' in data:
            listing.is_rented = data['is_rented']
        if 'owner_id' in data:
            listing.owner_id = int(data['owner_id'])
        db.session.commit()
        return success_response(listing.serialize())
    else:
        return failure_response("Method not supported", 400)

@app.route("/api/rentals/" , methods = ["GET","POST"])
def handle_rentals():
    if request.method == "GET":
        rentals = [r.serialize_no_user() for r in Rental.query.all()]
        return success_response({"listings": rentals})
    elif request.method == "POST":
        body = json.loads(request.data)
        required_fields = ['start_date', 'end_date', 'user_id', 'listing_id']
        #Check for missing fields here 
        if not all(field in body for field in required_fields):
            return failure_response("Missing required fields",400)
        #Convert the start_date and end_date
        start_date = datetime.strptime(body['start_date'], '%Y-%m-%d').date()
        end_date = datetime.strptime(body['end_date'], '%Y-%m-%d').date()
        #Is our start date greater than end
        if start_date > end_date:
            return failure_response("Start date must be before end_date")
        user = User.query.get(body['user_id'])
        listing = Listing.query.get(body['listing_id'])
        if not user or not listing:
            return failure_response("User or Listing not found")
        #Check if times make sense
        if start_date < listing.available_from or end_date > listing.available_to:
            return failure_response("Listing not avaiable from those times",400)
        overlapping = Rental.query.filter(
            Rental.listing_id == listing.id,
            Rental.end_date >= start_date,
            Rental.start_date <= end_date
        ).first()
        if overlapping:
            return failure_response("Listing")
        rental = Rental(
            start_date=start_date,
            end_date=end_date,
            user_id=user.id,
            listing_id=listing.id
        )
        listing.available_from = end_date + timedelta(days=1)
        db.session.add(rental)
        db.session.commit()
        return success_response(rental.serialize())
@app.route("/api/rentals/<int:rental_id>/" , methods = ["GET","DELETE"])
def handle_specific_rental(rental_id):
    if request.method == "GET":
        rental = Rental.query.filter_by(id=rental_id).first()
        if rental is None:
            return failure_response("Rental not found")
        return success_response(rental.serialize())
    if request.method == "DELETE":
        rental = Rental.query.filter_by(id=rental_id).first()
        listing = rental.listing  # Use the relationship
        # Restore listing availability
        listing.available_from = listing.original_available_from
        listing.available_to = listing.original_available_to
        listing.is_rented = False  # Optional: mark as not rented

        db.session.delete(rental)
        db.session.commit()
        return success_response(rental.serialize())


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000, debug=True)

