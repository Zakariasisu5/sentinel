# Security Model

Sentinel is designed as a local, simulation-first cybersecurity dashboard. It does not perform offensive security operations or unauthorized scanning.

## Detection engine

The application includes a safe local detection engine that models common suspicious patterns, such as repeated failed logins, suspicious connection bursts, and unusual frequencies.

## Safe usage constraints

- Only local simulated events are generated during the initial implementation.
- No credential harvesting, exploitation, or destructive actions are included.
- Real network monitoring should be added only with authorized data sources and proper user consent.

## Detection rules

The project separates detection rules into dedicated, reusable classes so they can be extended and tested independently.
