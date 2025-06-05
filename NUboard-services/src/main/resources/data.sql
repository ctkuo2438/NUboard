INSERT INTO locations (name) VALUES ('Boston')ON CONFLICT (name) DO NOTHING;
INSERT INTO locations (name) VALUES ('San Jose')ON CONFLICT (name) DO NOTHING;
INSERT INTO locations (name) VALUES ('Seattle') ON CONFLICT (name) DO NOTHING;

INSERT INTO colleges (name) VALUES ('College of Engineering') ON CONFLICT (name) DO NOTHING;
INSERT INTO colleges (name) VALUES ('Khoury College of Computer Sciences') ON CONFLICT (name) DO NOTHING;
INSERT INTO colleges (name) VALUES ('School of Law') ON CONFLICT (name) DO NOTHING;
