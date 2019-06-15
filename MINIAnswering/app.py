# -*- encoding=UTF-8 -*-
from flask_script import Manager, Shell
from flask_migrate import MigrateCommand, Migrate
from app import create_app, db
from flask_compress import Compress
from flask_htmlmin import HTMLMIN

app = create_app('default')
Compress(app)
HTMLMIN(app)
manager = Manager(app)
migrate = Migrate(app, db)


def make_shell_context():
    return dict(app=app, db=db)


manager.add_command('shell', Shell(make_context=make_shell_context))
manager.add_command('db', MigrateCommand)


if __name__ == '__main__':

    app.run(
        port=5005,
        host='0.0.0.0'
    )


