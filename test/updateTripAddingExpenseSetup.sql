TRUNCATE traveler_network CASCADE;

INSERT INTO traveler_network(id, version) VALUES (1, 0);

INSERT INTO tripter_user(id, email, first_name, last_name, password, username, version, traveler_network_id)
VALUES (2, 'traveler1', 'traveler1', 'traveler1', '$2a$10$0ZEck5JjGR3S2OlkgcXHb.DVO6MnEbfY78a4IlfSF/uZjc89j6cka', 'traveler1', 0, 1);

INSERT INTO tripter_user(id, email, first_name, last_name, password, username, version, traveler_network_id)
VALUES (3, 'traveler2', 'traveler2', 'traveler2', '$2a$10$0ZEck5JjGR3S2OlkgcXHb.DVO6MnEbfY78a4IlfSF/uZjc89j6cka', 'traveler2', 0, 1);

INSERT INTO tripter_user(id, email, first_name, last_name, password, username, version, traveler_network_id)
VALUES (4, 'traveler3', 'traveler3', 'traveler3', '$2a$10$0ZEck5JjGR3S2OlkgcXHb.DVO6MnEbfY78a4IlfSF/uZjc89j6cka', 'traveler3', 0, 1);

INSERT INTO trip(id, end_date, start_date, title, version, organizer_id)
VALUES (5, NULL, '2019-06-15', 'Remove a user from trip', 3, 2);

INSERT INTO user_account_for_trip(id, balance, version, trip_id, user_id)
VALUES (6, 0, 0, 5, 2);

INSERT INTO user_account_for_trip(id, balance, version, trip_id, user_id)
VALUES (7, 0, 0, 5, 3);

INSERT INTO user_account_for_trip(id, balance, version, trip_id, user_id)
VALUES (8, 0, 0, 5, 4);
