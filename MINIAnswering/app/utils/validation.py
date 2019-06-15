import re


def is_valid_email(email):
    if len(email) > 7:
        if re.match(r"^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$",
                    email.lower()) is not None:
            return True
    return False


def is_valid_password(password):
    if len(password) > 7 or len(password) < 30:
        # if re.match(r"^[0-9]{6,16}$|^[a-zA-Z]{6,16}$",
        #             password) is not None:
        return True
    return False


