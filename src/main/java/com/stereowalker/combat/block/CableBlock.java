package com.stereowalker.combat.block;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;
import com.stereowalker.combat.state.properties.BlockStatePropertiesList;
import com.stereowalker.combat.state.properties.CableConnectionType;
import com.stereowalker.combat.tags.CTags.BlockCTags;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LeadItem;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
public class CableBlock extends Block implements IBucketPickupHandler, ILiquidContainer {
	public static final EnumProperty<CableConnectionType> NORTH = BlockStatePropertiesList.NORTH_CONNECTION;
	public static final EnumProperty<CableConnectionType> EAST = BlockStatePropertiesList.EAST_CONNECTION;
	public static final EnumProperty<CableConnectionType> SOUTH = BlockStatePropertiesList.SOUTH_CONNECTION;
	public static final EnumProperty<CableConnectionType> WEST = BlockStatePropertiesList.WEST_CONNECTION;
	public static final EnumProperty<CableConnectionType> UP = BlockStatePropertiesList.UP_CONNECTION;
	public static final EnumProperty<CableConnectionType> DOWN = BlockStatePropertiesList.DOWN_CONNECTION;
	public static final BooleanProperty POWERED = BlockStatePropertiesList.POWERED;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final Map<Direction, EnumProperty<CableConnectionType>> FACING_TO_PROPERTY_MAP = Util.make(Maps.newEnumMap(Direction.class), (p_203421_0_) -> {
	      p_203421_0_.put(Direction.NORTH, NORTH);
	      p_203421_0_.put(Direction.EAST, EAST);
	      p_203421_0_.put(Direction.SOUTH, SOUTH);
	      p_203421_0_.put(Direction.WEST, WEST);
	      p_203421_0_.put(Direction.UP, UP);
	      p_203421_0_.put(Direction.DOWN, DOWN);
	   });
	protected static final Map<Direction, EnumProperty<CableConnectionType>> FACING_TO_PROPERTY_MAP_H = FACING_TO_PROPERTY_MAP.entrySet().stream().filter((p_199775_0_) -> {
		return p_199775_0_.getKey().getAxis().isHorizontal();
	}).collect(Util.toMapCollector());
	protected static final Map<Direction, EnumProperty<CableConnectionType>> FACING_TO_PROPERTY_MAP_V = FACING_TO_PROPERTY_MAP.entrySet().stream().filter((p_199775_0_) -> {
		return p_199775_0_.getKey().getAxis().isVertical();
	}).collect(Util.toMapCollector());
	protected final VoxelShape[] beamShape;

	public CableBlock(Properties properties) {
		super(properties);
		this.beamShape = this.beamShape();
		this.setDefaultState(this.stateContainer.getBaseState().with(UP, CableConnectionType.NONE).with(DOWN, CableConnectionType.NONE).with(NORTH, CableConnectionType.NONE).with(EAST, CableConnectionType.NONE).with(SOUTH, CableConnectionType.NONE).with(WEST, CableConnectionType.NONE).with(WATERLOGGED, Boolean.valueOf(false)).with(POWERED, Boolean.valueOf(false)));
	}

	protected VoxelShape[] beamShape() {
		VoxelShape voxelBase = Block.makeCuboidShape(7,7,7,9,9,9);
		VoxelShape voxelDown = Block.makeCuboidShape(7,0,7,9,9,9);
		VoxelShape voxelUp = Block.makeCuboidShape(7,7,7,9,16,9);
		VoxelShape voxelNorth = Block.makeCuboidShape(7,7,0,9,9,9);
		VoxelShape voxelSouth = Block.makeCuboidShape(7,7,7,9,9,16);
		VoxelShape voxelWest = Block.makeCuboidShape(0,7,7,9,9,9);
		VoxelShape voxelEast = Block.makeCuboidShape(7,7,7,16,9,9);
		VoxelShape voxelDU = VoxelShapes.or(voxelDown, voxelUp);
		VoxelShape voxelDN = VoxelShapes.or(voxelDown, voxelNorth);
		VoxelShape voxelDS = VoxelShapes.or(voxelDown, voxelSouth);
		VoxelShape voxelDW = VoxelShapes.or(voxelDown, voxelWest);
		VoxelShape voxelDE = VoxelShapes.or(voxelDown, voxelEast);
		VoxelShape voxelUN = VoxelShapes.or(voxelUp, voxelNorth);
		VoxelShape voxelUS = VoxelShapes.or(voxelUp, voxelSouth);
		VoxelShape voxelUW = VoxelShapes.or(voxelUp, voxelWest);
		VoxelShape voxelUE = VoxelShapes.or(voxelUp, voxelEast);
		VoxelShape voxelNS = VoxelShapes.or(voxelNorth, voxelSouth);
		VoxelShape voxelNW = VoxelShapes.or(voxelNorth, voxelWest);
		VoxelShape voxelNE = VoxelShapes.or(voxelNorth, voxelEast);
		VoxelShape voxelSW = VoxelShapes.or(voxelSouth, voxelWest);
		VoxelShape voxelSE = VoxelShapes.or(voxelSouth, voxelEast);
		VoxelShape voxelWE = VoxelShapes.or(voxelWest, voxelEast);
		VoxelShape voxelDUN = VoxelShapes.or(voxelDU, voxelNorth);
		VoxelShape voxelNSW = VoxelShapes.or(voxelNorth, voxelSW);
		VoxelShape voxelUNSW = VoxelShapes.or(voxelUp, voxelNSW);
		VoxelShape voxelSWE = VoxelShapes.or(voxelEast, voxelSW);
		VoxelShape voxelDUNSW = VoxelShapes.or(voxelDown, voxelUNSW);

		VoxelShape[] avoxelshape = new VoxelShape[]{VoxelShapes.empty(), voxelDown, voxelUp, voxelDU, voxelNorth, voxelDN, voxelUN, voxelDUN, voxelSouth, 
				VoxelShapes.or(voxelDown, voxelSouth), VoxelShapes.or(voxelUp, voxelSouth), VoxelShapes.or(voxelDU, voxelSouth), voxelNS, VoxelShapes.or(voxelNS, voxelDown), 
				VoxelShapes.or(voxelNS, voxelUp), VoxelShapes.or(voxelNS, voxelDU), voxelWest, VoxelShapes.or(voxelWest, voxelDown), VoxelShapes.or(voxelWest, voxelUp), 
				VoxelShapes.or(voxelWest, voxelDU), VoxelShapes.or(voxelWest, voxelNorth), VoxelShapes.or(voxelWest, voxelDN), VoxelShapes.or(voxelWest, voxelUN), 
				VoxelShapes.or(voxelWest, voxelDUN), voxelSW, VoxelShapes.or(voxelSW, voxelDown),  VoxelShapes.or(voxelSW, voxelUp), VoxelShapes.or(voxelSW, voxelDU),voxelNSW, 
				VoxelShapes.or(voxelNSW, voxelDown), voxelUNSW, voxelDUNSW, voxelEast, voxelDE, voxelUE, VoxelShapes.or(voxelEast, voxelDU), voxelNE, 
				VoxelShapes.or(voxelEast, voxelDN), VoxelShapes.or(voxelEast, voxelUN), VoxelShapes.or(voxelNE, voxelDU), voxelSE, VoxelShapes.or(voxelEast, voxelDS), 
				VoxelShapes.or(voxelEast, voxelUS), VoxelShapes.or(voxelSE, voxelDU), VoxelShapes.or(voxelEast, voxelNS), VoxelShapes.or(voxelSE, voxelDN), 
				VoxelShapes.or(voxelSE, voxelUN), VoxelShapes.or(voxelSE, voxelDUN), voxelWE, VoxelShapes.or(voxelEast, voxelDW), VoxelShapes.or(voxelEast, voxelUW), 
				VoxelShapes.or(voxelWE, voxelDU), VoxelShapes.or(voxelEast, voxelNW), VoxelShapes.or(voxelWE, voxelDN), VoxelShapes.or(voxelWE, voxelUN), 
				VoxelShapes.or(voxelWE, voxelDUN), VoxelShapes.or(voxelEast, voxelSW), VoxelShapes.or(voxelWE, voxelDS), VoxelShapes.or(voxelWE, voxelUS), 
				VoxelShapes.or(voxelSWE, voxelDU), VoxelShapes.or(voxelWE, voxelNS), VoxelShapes.or(voxelSWE, voxelDN), VoxelShapes.or(voxelSWE, voxelUN), 
				VoxelShapes.or(voxelSWE, voxelDUN)};

		for(int i = 0; i < 64; ++i) {
			avoxelshape[i] = VoxelShapes.or(voxelBase, avoxelshape[i]);
		}

		return avoxelshape;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.beamShape[this.getIndex(state)];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.beamShape[this.getIndex(state)];
	}

	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return this.beamShape[this.getIndex(state)];
	}

	private static int getMask(Direction p_196407_0_) {
		return 1 << p_196407_0_.getIndex();
	}

	protected int getIndex(BlockState p_196406_1_) {
		int i = 0;
		if (p_196406_1_.get(DOWN) != CableConnectionType.NONE) {
			i |= getMask(Direction.DOWN);
		}
		if (p_196406_1_.get(UP) != CableConnectionType.NONE) {
			i |= getMask(Direction.UP);
		}
		if (p_196406_1_.get(NORTH) != CableConnectionType.NONE) {
			i |= getMask(Direction.NORTH);
		}
		if (p_196406_1_.get(SOUTH) != CableConnectionType.NONE) {
			i |= getMask(Direction.SOUTH);
		}
		if (p_196406_1_.get(WEST) != CableConnectionType.NONE) {
			i |= getMask(Direction.WEST);
		}
		if (p_196406_1_.get(EAST) != CableConnectionType.NONE) {
			i |= getMask(Direction.EAST);
		}

		return i;
	}

	public Fluid pickupFluid(IWorld worldIn, BlockPos pos, BlockState state) {
		if (state.get(WATERLOGGED)) {
			worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(false)), 3);
			return Fluids.WATER;
		} else {
			return Fluids.EMPTY;
		}
	}

	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
		return !state.get(WATERLOGGED) && fluidIn == Fluids.WATER;
	}

	public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		if (!state.get(WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER) {
			if (!worldIn.isRemote()) {
				worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(true)), 3);
				worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
			}

			return true;
		} else {
			return false;
		}
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		switch(rot) {
		case CLOCKWISE_180:
			return state.with(NORTH, state.get(SOUTH)).with(EAST, state.get(WEST)).with(SOUTH, state.get(NORTH)).with(WEST, state.get(EAST));
		case COUNTERCLOCKWISE_90:
			return state.with(NORTH, state.get(EAST)).with(EAST, state.get(SOUTH)).with(SOUTH, state.get(WEST)).with(WEST, state.get(NORTH));
		case CLOCKWISE_90:
			return state.with(NORTH, state.get(WEST)).with(EAST, state.get(NORTH)).with(SOUTH, state.get(EAST)).with(WEST, state.get(SOUTH));
		default:
			return state;
		}
	}

	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		switch(mirrorIn) {
		case LEFT_RIGHT:
			return state.with(NORTH, state.get(SOUTH)).with(SOUTH, state.get(NORTH));
		case FRONT_BACK:
			return state.with(EAST, state.get(WEST)).with(WEST, state.get(EAST));
		default:
			return super.mirror(state, mirrorIn);
		}
	}

	public boolean isFullCube(BlockState state) {
		return false;
	}

	public ActionResultType onBlockActivated(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
	      if (p_225533_2_.isRemote) {
	         ItemStack itemstack = p_225533_4_.getHeldItem(p_225533_5_);
	         return itemstack.getItem() == Items.LEAD ? ActionResultType.SUCCESS : ActionResultType.PASS;
	      } else {
	         return LeadItem.bindPlayerMobs(p_225533_4_, p_225533_2_, p_225533_3_);
	      }
	   }

	public boolean canConnect(BlockState p_220111_1_, boolean p_220111_2_, Direction p_220111_3_) {
		Block block = p_220111_1_.getBlock();
		boolean flag = (block.isIn(BlockCTags.BEAMS) || block instanceof CableBlock) && p_220111_1_.getMaterial() == this.material;
		return !cannotAttach(block) && p_220111_2_ || flag;
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IBlockReader iblockreader = context.getWorld();
		BlockPos blockpos = context.getPos();
		FluidState FluidState = context.getWorld().getFluidState(context.getPos());
		BlockPos blockpos1 = blockpos.north();
		BlockPos blockpos2 = blockpos.east();
		BlockPos blockpos3 = blockpos.south();
		BlockPos blockpos4 = blockpos.west();
		BlockPos blockpos5 = blockpos.up();
		BlockPos blockpos6 = blockpos.down();
		BlockState blockstate = iblockreader.getBlockState(blockpos1);
		BlockState blockstate1 = iblockreader.getBlockState(blockpos2);
		BlockState blockstate2 = iblockreader.getBlockState(blockpos3);
		BlockState blockstate3 = iblockreader.getBlockState(blockpos4);
		BlockState blockstate4 = iblockreader.getBlockState(blockpos5);
		BlockState blockstate5 = iblockreader.getBlockState(blockpos6);
		return super.getStateForPlacement(context)
				.with(NORTH, Boolean.valueOf(this.canConnect(blockstate, blockstate.isSolidSide(iblockreader, blockpos1, Direction.SOUTH), Direction.SOUTH)) ? CableConnectionType.RECIEVE : CableConnectionType.NONE)
				.with(EAST, Boolean.valueOf(this.canConnect(blockstate1, blockstate1.isSolidSide(iblockreader, blockpos2, Direction.WEST), Direction.WEST)) ? CableConnectionType.RECIEVE : CableConnectionType.NONE)
				.with(SOUTH, Boolean.valueOf(this.canConnect(blockstate2, blockstate2.isSolidSide(iblockreader, blockpos3, Direction.NORTH), Direction.NORTH)) ? CableConnectionType.RECIEVE : CableConnectionType.NONE)
				.with(WEST, Boolean.valueOf(this.canConnect(blockstate3, blockstate3.isSolidSide(iblockreader, blockpos4, Direction.EAST), Direction.EAST)) ? CableConnectionType.RECIEVE : CableConnectionType.NONE)
				.with(UP, Boolean.valueOf(this.canConnect(blockstate4, blockstate4.isSolidSide(iblockreader, blockpos5, Direction.DOWN), Direction.DOWN)) ? CableConnectionType.RECIEVE : CableConnectionType.NONE)
				.with(DOWN, Boolean.valueOf(this.canConnect(blockstate5, blockstate5.isSolidSide(iblockreader, blockpos6, Direction.UP), Direction.UP)) ? CableConnectionType.RECIEVE : CableConnectionType.NONE)
				.with(WATERLOGGED, Boolean.valueOf(FluidState.getFluid() == Fluids.WATER));
	}
	
	public static EnumProperty<CableConnectionType> getConnectionFromDirection(Direction direction){
		if (direction == Direction.DOWN) return DOWN;
		else if (direction == Direction.EAST) return EAST;
		else if (direction == Direction.NORTH) return NORTH;
		else if (direction == Direction.SOUTH) return SOUTH;
		else if (direction == Direction.UP) return UP;
		else return WEST;
	}

	@SuppressWarnings("deprecation")
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		if(facing.getAxis().getPlane() == Direction.Plane.HORIZONTAL) {
			return stateIn.with(FACING_TO_PROPERTY_MAP_H.get(facing), Boolean.valueOf(this.canConnect(facingState, facingState.isSolidSide(worldIn, facingPos, facing.getOpposite()), facing.getOpposite())) ? CableConnectionType.RECIEVE : CableConnectionType.NONE);
		} else if(facing.getAxis().getPlane() == Direction.Plane.VERTICAL){
			return stateIn.with(FACING_TO_PROPERTY_MAP_V.get(facing), Boolean.valueOf(this.canConnect(facingState, facingState.isSolidSide(worldIn, facingPos, facing.getOpposite()), facing.getOpposite())) ? CableConnectionType.RECIEVE : CableConnectionType.NONE);
		}
		else return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		BlockState connectedTo;
		for (Direction direction : Direction.values()) {
			if (state.get(getConnectionFromDirection(direction)) == CableConnectionType.RECIEVE) {
				if (worldIn.getBlockState(pos.offset(direction)).getBlock() instanceof CableBlock) {
					connectedTo = worldIn.getBlockState(pos.offset(direction));
					if (connectedTo.get(getConnectionFromDirection(direction.getOpposite())) == CableConnectionType.TRANSFER) {
//						state.with(POWERED, connectedTo.get(POWERED));
						if (connectedTo.get(POWERED) != state.get(POWERED)) {
							worldIn.setBlockState(pos, state.with(POWERED, connectedTo.get(POWERED)));
						}
					}
				}
			}
		}
		super.tick(state, worldIn, pos, rand);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(UP, DOWN, NORTH, EAST, WEST, SOUTH, WATERLOGGED, POWERED);
	}
}
