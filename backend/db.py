from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()

# User Model
class User(db.Model):
    __tablename__ = 'user'
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String, nullable=False)
    email = db.Column(db.String, nullable=False, unique=True)

    listings = db.relationship('Listing', backref='owner', cascade="all, delete-orphan")
    rentals = db.relationship('Rental', backref='renter', cascade="all, delete-orphan")

    def serialize(self):
        return {
            "id": self.id,
            "name": self.name,
            "email": self.email,
            "listings": [l.serialize_no_user() for l in self.listings],
            "rentals": [r.serialize_no_user() for r in self.rentals]
        }

    def serialize_no_relations(self):
        return {
            "id": self.id,
            "name": self.name,
            "email": self.email
        }

# Lisiting Model
class Listing(db.Model):
    __tablename__ = 'listing'
    id = db.Column(db.Integer, primary_key=True)
    vehicle_type = db.Column(db.String, nullable=False)
    price_per_day = db.Column(db.Float, nullable=False)
    available_from = db.Column(db.Date, nullable=False)
    available_to = db.Column(db.Date, nullable=False)
    is_rented = db.Column(db.Boolean, default=False)

    owner_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)

    rentals = db.relationship('Rental', backref='listing', cascade="all, delete-orphan")

    def serialize(self):
        return {
            "id": self.id,
            "vehicle_type": self.vehicle_type,
            "price_per_day": self.price_per_day,
            "available_from": self.available_from.isoformat(),
            "available_to": self.available_to.isoformat(),
            "is_rented": self.is_rented,
            "owner": self.owner.serialize_no_relations()
        }

    def serialize_no_user(self):
        return {
            "id": self.id,
            "vehicle_type": self.vehicle_type,
            "price_per_day": self.price_per_day,
            "available_from": self.available_from.isoformat(),
            "available_to": self.available_to.isoformat(),
            "is_rented": self.is_rented
        }

# Rental Model
class Rental(db.Model):
    __tablename__ = 'rental'
    id = db.Column(db.Integer, primary_key=True)
    start_date = db.Column(db.Date, nullable=False)
    end_date = db.Column(db.Date, nullable=False)

    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    listing_id = db.Column(db.Integer, db.ForeignKey('listing.id'), nullable=False)

    def serialize(self):
        return {
            "id": self.id,
            "start_date": self.start_date.isoformat(),
            "end_date": self.end_date.isoformat(),
            "renter": self.renter.serialize_no_relations(),
            "listing": self.listing.serialize_no_user()
        }

    def serialize_no_user(self):
        return {
            "id": self.id,
            "start_date": self.start_date.isoformat(),
            "end_date": self.end_date.isoformat(),
            "listing_id": self.listing_id
        }
