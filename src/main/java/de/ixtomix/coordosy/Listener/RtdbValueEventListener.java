package de.ixtomix.coordosy.Listener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import de.ixtomix.coordosy.Coordosy;

public class RtdbValueEventListener implements ValueEventListener {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Coordosy.LOGGER.debug("RTDB-: " + dataSnapshot);
    }

    @Override
    public void onCancelled(DatabaseError error) {
        Coordosy.LOGGER.error("RTDB-: " + error);
    }
}
