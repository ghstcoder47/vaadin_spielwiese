## SQL migration file management

A hardlinked file to log required SQL changes for the current brach is availanle at `./sql/_current/changes.sql`.

This link is not tracked and regenereated on every git chackout!
 

### Setup git hook

run `./sql/setup.bat` (as admin if needed).  
only required when cloning a new repository.

### Tracking

Include all generated sql files when commiting.

Only edit through `./sql/_current/changes.sql` to keep consistency.

`./sql/master/changes.sql` is generated but ignored! 

###  Releasing

When changing to a `release/...` branch, all changes made to `develop` and inside `features` are merged into `release/.../changes.sql`, and the original files deleted.

only files inside `.sql/release/.../` are to be used for database migration on releases.

If needed, (soft) revert current working copy to latest commit to restore old changefiles, but delete them manually before commiting on release. 