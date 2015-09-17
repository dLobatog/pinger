# pinger

Get a periodic email that reminds you to reconnect with people. :two_women_holding_hands: :couple: :two_men_holding_hands:

## Build

Build the app into an uberjar (standalone jar) using Leiningen. Run:

    $ lein uberjar

and you will find the executable in `target/uberjar/pinger-version-standalone.jar`.

## Usage

Configure it by creating a file in your user home directory `~/.pinger` which contains
an email on the first line and the email password on the second line. Future versions
of pinger should support GPG decrypting of the password.

    sampleemail@sampledomain.com
    samplepassword

You should also have a list of people you want to periodically ping. Any plaintext file
with names should work.

    John Doe
    Jane Doe

`pinger` will pick the person from this list that you've pinged the least (or a random one if
you have just started), send you an email to remind you to ping this person, and add a counter
next to its name in the list.

Run it passing the list of people as an argument using:

    $ ./pinger list_of_people_to_ping

or this if you are in a non-UNIX environment.

    $ java -jar pinger-version-standalone.jar list_of_people_to_ping

I recommend to run it using a cronjob every week (or whatever frequency you prefer!)

## TODO

* GPG-decrypt email password
* Packaging (rpm/deb/homebrew)
* Script to add pinger to crontab

## License

Copyright © 2015 Daniel Lobato García

GPLv3 licensed, see [LICENSE](LICENSE) for details.
