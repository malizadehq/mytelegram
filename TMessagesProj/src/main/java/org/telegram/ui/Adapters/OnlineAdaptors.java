package org.telegram.ui.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.GreySectionCell;
import org.telegram.ui.Cells.LetterSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.UserCell;

import java.util.ArrayList;
import java.util.HashMap;

public class OnlineAdaptors extends BaseSectionsAdapter {

    private Context mContext;
    private int onlyUsers;
    private boolean needPhonebook;
    private HashMap<Integer, TLRPC.User> ignoreUsers;
    private HashMap<Integer, ?> checkedMap;
    private boolean scrolling;
    private boolean isAdmin;

    public OnlineAdaptors(Context context, int onlyUsersType, boolean arg2, HashMap<Integer, TLRPC.User> arg3, boolean arg4) {
        mContext = context;
        onlyUsers = onlyUsersType;
        needPhonebook = arg2;
        ignoreUsers = arg3;
        isAdmin = arg4;
    }

    public void setCheckedMap(HashMap<Integer, ?> map) {
        checkedMap = map;
    }

    public void setIsScrolling(boolean value) {
        scrolling = value;
    }

    @Override
    public Object getItem(int section, int position) {
        HashMap<String, ArrayList<TLRPC.TL_contact>> onlineUsersSectionsDict = onlyUsers == 2 ? ContactsController.getInstance().onlineUsersMutualSectionsDict : ContactsController.getInstance().onlineUsersSectionsDict;
        ArrayList<String> onlineSortedUsersSectionsArray = onlyUsers == 2 ? ContactsController.getInstance().onlineSortedUsersMutualSectionsArray : ContactsController.getInstance().onlineSortedUsersSectionsArray;

        if (onlyUsers != 0 && !isAdmin) {
            if (section < onlineSortedUsersSectionsArray.size()) {
                ArrayList<TLRPC.TL_contact> arr = onlineUsersSectionsDict.get(onlineSortedUsersSectionsArray.get(section));
                if (position < arr.size()) {
                    return MessagesController.getInstance().getUser(arr.get(position).user_id);
                }
            }
            return null;
        } else {
            if (section == 0) {
                return null;
            } else {
                if (section - 1 < onlineSortedUsersSectionsArray.size()) {
                    ArrayList<TLRPC.TL_contact> arr = onlineUsersSectionsDict.get(onlineSortedUsersSectionsArray.get(section - 1));
                    if (position < arr.size()) {
                        return MessagesController.getInstance().getUser(arr.get(position).user_id);
                    }
                    return null;
                }
            }
        }
        if (needPhonebook) {
            return ContactsController.getInstance().phoneBookContacts.get(position);
        }
        return null;
    }

    @Override
    public boolean isRowEnabled(int section, int row) {
        HashMap<String, ArrayList<TLRPC.TL_contact>> onlineUsersSectionsDict = onlyUsers == 2 ? ContactsController.getInstance().onlineUsersMutualSectionsDict : ContactsController.getInstance().onlineUsersSectionsDict;
        ArrayList<String> onlineSortedUsersSectionsArray = onlyUsers == 2 ? ContactsController.getInstance().onlineSortedUsersMutualSectionsArray : ContactsController.getInstance().onlineSortedUsersSectionsArray;

        if (onlyUsers != 0 && !isAdmin) {
            ArrayList<TLRPC.TL_contact> arr = onlineUsersSectionsDict.get(onlineSortedUsersSectionsArray.get(section));
            return row < arr.size();
        } else {
            if (section == 0) {
                if (needPhonebook || isAdmin) {
                    if (row == 1) {
                        return false;
                    }
                } else {
                    if (row == 3) {
                        return false;
                    }
                }
                return true;
            } else if (section - 1 < onlineSortedUsersSectionsArray.size()) {
                ArrayList<TLRPC.TL_contact> arr = onlineUsersSectionsDict.get(onlineSortedUsersSectionsArray.get(section - 1));
                return row < arr.size();
            }
        }
        return true;
    }

    @Override
    public int getSectionCount() {
        ArrayList<String> onlineSortedUsersSectionsArray = onlyUsers == 2 ? ContactsController.getInstance().onlineSortedUsersMutualSectionsArray : ContactsController.getInstance().onlineSortedUsersSectionsArray;
        int count = onlineSortedUsersSectionsArray.size();
        if (onlyUsers == 0) {
            count++;
        }
        if (isAdmin) {
            count++;
        }
        if (needPhonebook) {
            count++;
        }
        return count;
    }

    @Override
    public int getCountForSection(int section) {
        HashMap<String, ArrayList<TLRPC.TL_contact>> onlineUsersSectionsDict = onlyUsers == 2 ? ContactsController.getInstance().onlineUsersMutualSectionsDict : ContactsController.getInstance().onlineUsersSectionsDict;
        ArrayList<String> onlineSortedUsersSectionsArray = onlyUsers == 2 ? ContactsController.getInstance().onlineSortedUsersMutualSectionsArray : ContactsController.getInstance().onlineSortedUsersSectionsArray;

        if (onlyUsers != 0 && !isAdmin) {
            if (section < onlineSortedUsersSectionsArray.size()) {
                ArrayList<TLRPC.TL_contact> arr = onlineUsersSectionsDict.get(onlineSortedUsersSectionsArray.get(section));
                int count = arr.size();
                if (section != (onlineSortedUsersSectionsArray.size() - 1) || needPhonebook) {
                    count++;
                }
                return count;
            }
        } else {
            if (section == 0) {
                if (needPhonebook || isAdmin) {
                    return 2;
                } else {
                    return 4;
                }
            } else if (section - 1 < onlineSortedUsersSectionsArray.size()) {
                ArrayList<TLRPC.TL_contact> arr = onlineUsersSectionsDict.get(onlineSortedUsersSectionsArray.get(section - 1));
                int count = arr.size();
                if (section - 1 != (onlineSortedUsersSectionsArray.size() - 1) || needPhonebook) {
                    count++;
                }
                return count;
            }
        }
        if (needPhonebook) {
            return ContactsController.getInstance().phoneBookContacts.size();
        }
        return 0;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        HashMap<String, ArrayList<TLRPC.TL_contact>> onlineUsersSectionsDict = onlyUsers == 2 ? ContactsController.getInstance().onlineUsersMutualSectionsDict : ContactsController.getInstance().onlineUsersSectionsDict;
        ArrayList<String> onlineSortedUsersSectionsArray = onlyUsers == 2 ? ContactsController.getInstance().onlineSortedUsersMutualSectionsArray : ContactsController.getInstance().onlineSortedUsersSectionsArray;

        if (convertView == null) {
            convertView = new LetterSectionCell(mContext);
        }
        if (onlyUsers != 0 && !isAdmin) {
            if (section < onlineSortedUsersSectionsArray.size()) {
                ((LetterSectionCell) convertView).setLetter(onlineSortedUsersSectionsArray.get(section));
            } else {
                ((LetterSectionCell) convertView).setLetter("");
            }
        } else {
            if (section == 0) {
                ((LetterSectionCell) convertView).setLetter("");
            } else if (section - 1 < onlineSortedUsersSectionsArray.size()) {
                ((LetterSectionCell) convertView).setLetter(onlineSortedUsersSectionsArray.get(section - 1));
            } else {
                ((LetterSectionCell) convertView).setLetter("");
            }
        }
        return convertView;
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(section, position);
        if (type == 4) {
            if (convertView == null) {
                convertView = new DividerCell(mContext);
                convertView.setPadding(AndroidUtilities.dp(LocaleController.isRTL ? 28 : 72), 0, AndroidUtilities.dp(LocaleController.isRTL ? 72 : 28), 0);
            }
        } else if (type == 3) {
            if (convertView == null) {
                convertView = new GreySectionCell(mContext);
                ((GreySectionCell) convertView).setText(LocaleController.getString("Contacts", R.string.Contacts).toUpperCase());
            }
        } else if (type == 2) {
            if (convertView == null) {
                convertView = new TextCell(mContext);
            }
            TextCell actionCell = (TextCell) convertView;
            if (needPhonebook) {
                actionCell.setTextAndIcon(LocaleController.getString("InviteFriends", R.string.InviteFriends), R.drawable.menu_invite);
            } else if (isAdmin) {
                actionCell.setTextAndIcon(LocaleController.getString("InviteToGroupByLink", R.string.InviteToGroupByLink), R.drawable.menu_invite);
            } else {
                if (position == 0) {
                    actionCell.setTextAndIcon(LocaleController.getString("NewGroup", R.string.NewGroup), R.drawable.menu_newgroup);
                } else if (position == 1) {
                    actionCell.setTextAndIcon(LocaleController.getString("NewSecretChat", R.string.NewSecretChat), R.drawable.menu_secret);
                } else if (position == 2) {
                    actionCell.setTextAndIcon(LocaleController.getString("NewChannel", R.string.NewChannel), R.drawable.menu_broadcast);
                }
            }
        } else if (type == 1) {
            if (convertView == null) {
                convertView = new TextCell(mContext);
            }
            ContactsController.Contact contact = ContactsController.getInstance().phoneBookContacts.get(position);
            if (contact.first_name != null && contact.last_name != null) {
                ((TextCell) convertView).setText(contact.first_name + " " + contact.last_name);
            } else if (contact.first_name != null && contact.last_name == null) {
                ((TextCell) convertView).setText(contact.first_name);
            } else {
                ((TextCell) convertView).setText(contact.last_name);
            }
            convertView.setVisibility(View.GONE);
        } else if (type == 0) {
            if (convertView == null) {
                convertView = new UserCell(mContext, 58, 1, false);
                ((UserCell) convertView).setStatusColors(0xffa8a8a8, 0xff3b84c0);
            }

            HashMap<String, ArrayList<TLRPC.TL_contact>> onlineUsersSectionsDict = onlyUsers == 2 ? ContactsController.getInstance().onlineUsersMutualSectionsDict : ContactsController.getInstance().onlineUsersSectionsDict;
            ArrayList<String> onlineSortedUsersSectionsArray = onlyUsers == 2 ? ContactsController.getInstance().onlineSortedUsersMutualSectionsArray : ContactsController.getInstance().onlineSortedUsersSectionsArray;

            ArrayList<TLRPC.TL_contact> arr = onlineUsersSectionsDict.get(onlineSortedUsersSectionsArray.get(section - (onlyUsers != 0 && !isAdmin ? 0 : 1)));
            TLRPC.User user = MessagesController.getInstance().getUser(arr.get(position).user_id);
            ((UserCell) convertView).setData(user, null, null, 0);
            if (checkedMap != null) {
                ((UserCell) convertView).setChecked(checkedMap.containsKey(user.id), !scrolling && Build.VERSION.SDK_INT > 10);
            }
            if (ignoreUsers != null) {
                if (ignoreUsers.containsKey(user.id)) {
                    convertView.setAlpha(0.5f);
                } else {
                    convertView.setAlpha(1.0f);
                }
            }
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int section, int position) {
        HashMap<String, ArrayList<TLRPC.TL_contact>> onlineUsersSectionsDict = onlyUsers == 2 ? ContactsController.getInstance().onlineUsersMutualSectionsDict : ContactsController.getInstance().onlineUsersSectionsDict;
        ArrayList<String> onlineSortedUsersSectionsArray = onlyUsers == 2 ? ContactsController.getInstance().onlineSortedUsersMutualSectionsArray : ContactsController.getInstance().onlineSortedUsersSectionsArray;
        if (onlyUsers != 0 && !isAdmin) {
            ArrayList<TLRPC.TL_contact> arr = onlineUsersSectionsDict.get(onlineSortedUsersSectionsArray.get(section));
            return position < arr.size() ? 0 : 4;
        } else {
            if (section == 0) {
                if (needPhonebook || isAdmin) {
                    if (position == 1) {
                        return 3;
                    }
                } else {
                    if (position == 3) {
                        return 3;
                    }
                }
                return 2;
            } else if (section - 1 < onlineSortedUsersSectionsArray.size()) {
                ArrayList<TLRPC.TL_contact> arr = onlineUsersSectionsDict.get(onlineSortedUsersSectionsArray.get(section - 1));
                return position < arr.size() ? 0 : 4;
            }
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }
}
