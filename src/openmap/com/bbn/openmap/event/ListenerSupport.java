// **********************************************************************
// 
// <copyright>
// 
//  BBN Technologies
//  10 Moulton Street
//  Cambridge, MA 02138
//  (617) 873-8000
// 
//  Copyright (C) BBNT Solutions LLC. All rights reserved.
// 
// </copyright>
// **********************************************************************
// 
// $Source: /cvs/distapps/openmap/src/openmap/com/bbn/openmap/event/ListenerSupport.java,v $
// $RCSfile: ListenerSupport.java,v $
// $Revision: 1.6 $
// $Date: 2008/10/16 19:33:08 $
// $Author: dietrick $
// 
// **********************************************************************

package com.bbn.openmap.event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import com.bbn.openmap.util.Debug;

/**
 * This is an utility class that can be used by beans that need support for
 * handling listeners and firing events. You can use an subclass of this class
 * as a member field of your bean and delegate work to it. It handles the work
 * for a listener support subclass managing the Vector of listeners. It knows
 * nothing about firing events to the listeners.
 */
public class ListenerSupport<E> extends ArrayList<E> implements
        java.io.Serializable {

    // transient protected Vector<Object> listeners;

    protected Object source;

    /**
     * Construct a ListenerSupport object.
     * 
     * @param sourceBean The bean to be given as the source for any events.
     */
    public ListenerSupport(Object sourceBean) {
        setSource(sourceBean);
        Debug.message("listenersupport", "ListenerSupport()");
    }

    /**
     * Set the source of the events.
     */
    protected void setSource(Object src) {
        source = src;
    }

    /**
     * Get the source of the events.
     */
    protected Object getSource() {
        return source;
    }

    /**
     * Return an iterator over a clone of the listeners.
     */
    public synchronized Iterator<E> iterator() {
        ArrayList<E> v = new ArrayList<E>(this);
        return v.iterator();
    }

    /**
     * Write the listeners to a stream.
     */
    public void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();

        for (E e : this) {
            if (e instanceof Serializable) {
                s.writeObject(e);
            }
        }

        s.writeObject(null);
    }

    /**
     * Read the listeners from a stream.
     */
    public void readObject(ObjectInputStream s) throws ClassNotFoundException,
            IOException {

        s.defaultReadObject();

        Object listenerOrNull;
        while (null != (listenerOrNull = s.readObject())) {
            add((E) listenerOrNull);
        }
    }
}