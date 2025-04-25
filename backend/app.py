from flask import Flask
from flask_cors import CORS


app = Flask(__name__)
CORS(app)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///app.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

from models.models import db
db.init_app(app)

app.register_blueprint(auth_bp, url_prefix='/auth')
app.register_blueprint(listing_bp, url_prefix='/listings')

if __name__ == '__main__':
    with app.app_context():
        db.create_all()
    app.run(debug=True)
