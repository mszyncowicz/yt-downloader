# yt-downloader

YT-downloader is a JEE api for youtube-dl. It allows the user to download music or videos using youtube-dl within a browser. Application was designed for wildfly 24.1.0 to run as a windows service. Although, it preforms session creation for every usage, using this app wasn't created to serve more than one user. Application always store the files to user set direcitories which could be different for videos and music and do not pass this files back to a client. To support that you would need to create a new rest endpoint that would send the file back to the site.

Requirements:

- yt-dlp (youtube-dl is no longer supported) and ffmpeg installed in system environment variables.
- wildfly 24.0.0 or newer.
- maven and java for build

Default context root is host:port/DYT/ 

there is sepearate repository for the front which works at host:port/YT/ 
