**Rules:** Rules are predefined constraints and guidelines which help in PCB design

**BoardRules.java:** This file contains functions that help provide features in the rules drop down menu like adding, removing, and modifying values of Vias, nets, net classes, or clearance matrix.

This file contains functions like:

- **get_trace_half_width()** which returns the trace halfwidth used for routing with the input net on the input layer.
- **set_default_trace_half_width()** which Changes the default trace halfwidth used for routing on all layers to the input value.
- **get_default_net_class()** which returns the default net rule for which no special rule was set.
- **create_default_via_rule()** which helps Create a default via rule for p_net_class with name p_name. If more than one via infos with the same layer range are found, only the via info with the smallest pad size is inserted.
- **change_clearance_class_no()** which Changes the clearance class index of all objects on the board
- **remove_clearance_class()** which removes a clearance class provided an index.
- **get_igonre_conducion()** which if set true makes router ignore conduction areas.

**ClearanceMatrix.java:** This file addresses the features in the clearance matrix with help of functions.

- **remove_class():** Removes the class with index p_index from the clearance matrix.
- **append_class():** Appends a new clearance class to the clearance matrix and initializes it with the values of the default class. Returns false, if a clearance class with name p_class_name is already existing.
- **clearance_compensation_value():** Returns the clearance compensation value of p_clearance_class_no on layer p_layer.
- **get_layer_count():** Return the layer count of this clearance matrix

It consists of classes like:

- **Row:** contains a row of entries of the clearance matrix
- **MatrixEntry:** this is equivalent to a single entry of the clearance matrix

**DefaultItemClearanceClasses.java:**

A clearance class is a name attached with a set of specific rules(clearance) for the PCB.  
The file DefaultItemClearanceClasses.java enables functions for the inner working of clearance matrix.

- **get():** Returns the number of the default clearance class for the input item class.
- **set():** Sets the index of the default clearance class of the input item class in the clearance matrix to p_index.
- **set_all():** Sets the indices of all default item clearance classes to p_index.

**Net.java:**

This file describes properties for an electrical net.

- **get_terminal_items():** Returns the pins and conduction areas of this net.
- **get_pins():** Returns the pins of this net.
- **get_items():** Returns all items of this net.
- **get_trace_length():** Returns the cumulative trace length of all traces on the board belonging to this net.
- **get_via_count():** Returns the count of vias on the board belonging to this net.
- **contains_plane():** Indicates, if this net contains a power plane. Used by the autorouter for setting the via costs to the cheap plane via costs. May also be true, if a layer covered with a conduction area of this net is a signal layer.

**NetClass.java:**

This file describes routing rules for the individual nets.

- **trace_width_is_layer_dependent():** Returns true, if the trace width of this class is not equal on all layers.
- **trace_width_is_inner_layer_dependent():** Returns true, if the trace width of this class is not equal on all inner layers.
- **set_all_inner_layers_active():** Activates or deactivates all inner layers for routing
- **is_active_routing-layer():** Returns if the layer with index p_layer_no is active for routing

**NetClasses.java:**

Contains an array of Net Classes for routing stores in a vector.

**Nets.java:**

Describes the electrical nets on the board. Contains an array of Nets in the form of a vector.

- **max_net_no():** Returns the biggest net number on the board.
- **get():** Used to return net with help of a value like input net number or name.
- **new_net():** Creates a new number, and adds it to the end of the nets array.
- **add():** Adds a new net with default properties with the input name.
- **get_board():** Gets the board of this net list. Used to get access to the items of the net.
- **set_board():** Sets the board of this net list. Used to get access to the items of the net.

**ViaInfo.java:**

Information about a combination of via_padstack, via clearance class and drill_to_smd_allowed used in interactive and auto-routing.

- **get_name():** Gets name of the Via selected.
- **set_name():** Sets name of the Via selected to the input value.
- **attach_smd_allowed():** checks if it allows attachment with smd.
- **set_attach_smd_allowed():** sets attachment with smd.
- **compareTo():** compares two vias.

**ViaInfos.java:**

Contains a list of different ViaInfo, which can be used in interactive and automatic routing.

- **add():** adds a new Via Info to the list.
- **count():** Counts total number of vias in the list.
- **get():** Returns Via Info provided the name or number.
- **name_exists():** Checks if a via with the given input name exists.
- **remove():** Removes a ViaInfo(given) from the list of Via Infos.

**ViaRule.java:**

Contains an array of vias used for routing. Vias at the beginning of the array are preferred to later vias.

- **remove_via():** Removes via from the rule, returns false if via is not in the rule.
- **contains():** Checks if given viaInfo in the ViaInfos list is in the ViaRule.
- **contains_padstack():** Returns true, if this via contains a padstack(given).
- **get_layer_range():** Searches for Via in the ViaRule from a one given layer to another given layer.
- **swap():** Swaps ViaInfo of 2 given ViaInfos, returns false if they were not found.
