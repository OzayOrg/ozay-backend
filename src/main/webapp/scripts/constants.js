'use strict';

/* Constants */

ozayApp.constant('USER_ROLES', {
        'all': '*',
        'admin': 'ROLE_ADMIN',
        'user': 'ROLE_USER',
        'access_directory_edit': 'DIRECTORY_EDIT',
        'access_directory_list': 'DIRECTORY_LIST',
        'access_directory_delete': 'DIRECTORY_DELETE',
        'access_notification_archive': 'NOTIFICATION_ARCHIVE',
        'access_notification_create': 'NOTIFICATION_CREATE',
        'access_notification_track': 'NOTIFICATION_TRACK',
        'access_management': 'ROLE_MANAGEMENT',
        'subscriber': 'ROLE_SUBSCRIBER',
    });

/*
Languages codes are ISO_639-1 codes, see http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
They are written in English to avoid character encoding issues (not a perfect solution)
*/
ozayApp.constant('LANGUAGES', {
        'ca': 'Catalan',
        'zh-tw': 'Chinese (traditional)',
        'da': 'Danish',
        'en': 'English',
        'fr': 'French',
        'de': 'German',
        'kr': 'Korean',
        'pl': 'Polish',
        'pt-br': 'Portuguese (Brazilian)',
        'ru': 'Russian',
        'es': 'Spanish',
        'sv': 'Swedish',
        'tr': 'Turkish'
    });
